package gps.base.service;

import gps.base.DTO.CommentDTO;
import gps.base.DTO.ReviewDTO;
import gps.base.model.Comment;
import gps.base.model.Image;
import gps.base.model.Member;
import gps.base.model.Review;
import gps.base.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private GymRepository gymRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ReviewService reviewService;

    private Review testReview;
    private ReviewDTO testReviewDTO;
    private Member testMember;
    private Image testImage;
    private MockMultipartFile testFile;

    @BeforeEach
    void setUp() {
        // 테스트용 업로드 경로 설정
        ReflectionTestUtils.setField(reviewService, "uploadPath", "test-uploads");

        // 테스트 데이터 설정
        testMember = new Member();
        testMember.setUserId(1L);
        testMember.setName("Test User");

        testReview = new Review();
        testReview.setRId(1L);
        testReview.setUserId(1L);
        testReview.setGymId(1L);
        testReview.setComment("Test Comment");
        testReview.setAddedAt(LocalDateTime.now());

        testReviewDTO = new ReviewDTO();
        testReviewDTO.setUserId(1L);
        testReviewDTO.setGymId(1L);
        testReviewDTO.setComment("Test Comment");

        testImage = new Image();
        testImage.setId(1L);
        testImage.setImageUrl("test_image.jpg");
        testImage.setCaption("Test Caption");
        testImage.setUserId(1L);
        testImage.setGymId(1L);
        testImage.setReviewId(1L);

        testFile = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
    }

    @Test
    void createReview_WithImage_Success() throws IOException, GymNotFoundException {
        // Given
        when(gymRepository.existsById(anyLong())).thenReturn(true);
        when(reviewRepository.save(any(Review.class))).thenReturn(testReview);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(testMember));
        when(imageRepository.save(any(Image.class))).thenReturn(testImage);

        // When
        ReviewDTO result = reviewService.createReview(testReviewDTO, testFile);

        // Then
        assertNotNull(result);
        assertEquals(testReview.getRId(), result.getRId());
        assertEquals(testReview.getComment(), result.getComment());
        verify(imageRepository).save(any(Image.class));
        verify(messagingTemplate).convertAndSend(anyString(), anyString());
    }

    @Test
    void createReview_WithoutImage_Success() throws IOException, GymNotFoundException {
        // Given
        when(gymRepository.existsById(anyLong())).thenReturn(true);
        when(reviewRepository.save(any(Review.class))).thenReturn(testReview);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(testMember));

        // When
        ReviewDTO result = reviewService.createReview(testReviewDTO, null);

        // Then
        assertNotNull(result);
        assertEquals(testReview.getRId(), result.getRId());
        assertEquals(testReview.getComment(), result.getComment());
        verify(imageRepository, never()).save(any(Image.class));
    }

    @Test
    void createReview_GymNotFound() {
        // Given
        when(gymRepository.existsById(anyLong())).thenReturn(false);

        // Then
        assertThrows(GymNotFoundException.class, () -> {
            reviewService.createReview(testReviewDTO, testFile);
        });
    }

    @Test
    void getAllReviewsWithUserName_Success() {
        // Given
        List<ReviewDTO> reviews = Arrays.asList(testReviewDTO);
        when(reviewRepository.findAllReviewsWithUserName()).thenReturn(reviews);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(testMember));
        when(imageRepository.findByReviewId(anyLong())).thenReturn(Arrays.asList(testImage));

        // When
        List<ReviewDTO> result = reviewService.getAllReviewsWithUserName();

        // Then
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(testMember.getName(), result.get(0).getUserName());
        assertEquals(testImage.getImageUrl(), result.get(0).getReviewImage());
    }

    @Test
    void deleteReview_Success() {
        // Given
        when(reviewRepository.findByrIdAndGymIdAndUserId(anyLong(), anyLong(), anyLong()))
                .thenReturn(Optional.of(testReview));

        // When
        reviewService.deleteReview(1L, 1L, 1L);

        // Then
        verify(imageRepository).deleteByReviewId(1L);
        verify(reviewRepository).delete(testReview);
        verify(messagingTemplate).convertAndSend(anyString(), anyString());
    }

    @Test
    void getReviewsByGym_Success() {
        // Given
        List<Review> expectedReviews = Arrays.asList(testReview);
        when(reviewRepository.findByGymId(anyLong())).thenReturn(expectedReviews);

        // When
        List<Review> result = reviewService.getReviewsByGym(1L);

        // Then
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(testReview.getRId(), result.get(0).getRId());
    }

    @Test
    void addComment_Success() {
        // Given
        Comment testComment = new Comment();
        testComment.setUserId(1L);
        testComment.setComment("Test Comment");
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setComment("Test Comment");

        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(testReview));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        // When
        Comment result = reviewService.addComment(1L, 1L, commentDTO);

        // Then
        assertNotNull(result);
        assertEquals(testComment.getComment(), result.getComment());
        verify(messagingTemplate).convertAndSend(anyString(), anyString());
    }

    @Test
    void updateComment_Success() {
        // Given
        Comment testComment = new Comment();
        testComment.setUserId(1L);
        testComment.setComment("Original Comment");
        testComment.setReview(testReview);

        CommentDTO updateDTO = new CommentDTO();
        updateDTO.setComment("Updated Comment");

        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(testReview));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(testComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        // When
        Comment result = reviewService.updateComment(1L, 1L, 1L, updateDTO);

        // Then
        assertNotNull(result);
        verify(commentRepository).save(any(Comment.class));
        verify(messagingTemplate).convertAndSend(anyString(), anyString());
    }
}
