package gps.base.service;


import gps.base.DTO.CommentDTO;
import gps.base.DTO.ReviewDTO;
import gps.base.model.Comment;
import gps.base.model.Review;
import gps.base.repository.CommentRepository;
import gps.base.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    /*
    리뷰가 정상적으로 생성되고 저장되는지, WebSocket 알림이 전송 되는지 확인 - MoonGwangHee
     */
    @Test
    void createReview() {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setUserId(1L);
        reviewDTO.setGymId(1L);
        reviewDTO.setComment("Great Gym!");


        Review savedReview = new Review();
        savedReview.setUserId(1L);
        savedReview.setGymId(1L);
        savedReview.setComment("Great Gym!");

        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);

        Review result = reviewService.createReview(reviewDTO);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("Great Gym!", result.getComment());
        verify(messagingTemplate).convertAndSend(eq("/topic/gym/1"), anyString());
    }

    /*
    특정 헬스장의 리뷰들을 정상적으로 조회하는지 확인 - MoonGwangHee
     */
    @Test
    void getReviewByGym() {
        Long gymId = 1L;
        List<Review> reviews = Arrays.asList(new Review(), new Review());
        when(reviewRepository.findByGymId(gymId)).thenReturn(reviews);

        List<Review> result = reviewService.getReviewsByGym(gymId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(reviewRepository).findByGymId(gymId);
    }


    /*
    댓글이 정상적으로 추가되고 저장되는지, Websocket 알림이 전송 되는지 확인
     */
    @Test
    void addComment() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setUserId(1L);
        commentDTO.setGymId(1L);
        commentDTO.setComment("좋은 시설이네요!");

        Comment savedComment = new Comment();
        savedComment.setUserId(1L);
        savedComment.setGymId(1L);
        savedComment.setComment("좋은 시설이네요!");

        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        Comment result = reviewService.addComment(commentDTO);


        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("좋은 시설이네요!", result.getComment());
        verify(messagingTemplate).convertAndSend(eq("/topic/gym/1"), anyString());
    }


    /*
    특정 헬스장의 댓글들을 정상적으로 조회하는지 확인 - MoonGwangHee
     */
    @Test
    void getCommentsByGym() {
        Long gymId = 1L;
        List<Comment> comments = Arrays.asList(new Comment(), new Comment());
        when(commentRepository.findByGymId(gymId)).thenReturn(comments);

        List<Comment> result = reviewService.getCommentsByGym(gymId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(commentRepository).findByGymId(gymId);
    }


    /*
    리뷰 수정 성공 확인 - MoonGwangHee
     */
    @Test
    void updateReview_Success() {
        Long gymId = 1L;
        Long userId = 1L;
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setComment("수정된 댓글");
        
        Review savedReview = new Review();
        savedReview.setUserId(userId);
        savedReview.setGymId(gymId);
        savedReview.setComment("수정된 댓글");

        when(reviewRepository.findByGymIdAndUserId(gymId, userId)).thenReturn(Optional.of(savedReview));
        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);

        Review updatedReview = reviewService.updateReview(gymId, userId, reviewDTO);

        assertNotNull(updatedReview);
        assertEquals("수정된 댓글", updatedReview.getComment());
        verify(messagingTemplate).convertAndSend(eq("/topic/gym/" + gymId), anyString());

    }


    /*
    리뷰 수정 실패 (리뷰를 찾지 못할 때)
     */
    @Test
    void updateReview_ReviewNotFound() {
        Long gymId = 1L;
        Long userId = 1L;
        ReviewDTO reviewDTO = new ReviewDTO();

        when(reviewRepository.findByGymIdAndUserId(gymId, userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reviewService.updateReview(gymId, userId, reviewDTO));
    }



    /*
    리뷰 삭제 성공 - MoonGwangHee
     */
    @Test
    void deleteReview_Success() {
        Long gymId = 1L;
        Long userId = 1L;

        Review savedReview = new Review();
        savedReview.setUserId(userId);
        savedReview.setGymId(gymId);

        when(reviewRepository.findByGymIdAndUserId(gymId, userId)).thenReturn(Optional.of(savedReview));

        assertDoesNotThrow(() -> reviewService.deleteReview(gymId, userId));
        verify(reviewRepository).delete(savedReview);
        verify(messagingTemplate).convertAndSend(eq("/topic/gym/" + gymId), anyString());
    }


    /*
    리뷰 삭제 실패 (리뷰를 찾지 못할 때)
     */
    @Test
    void deleteReview_ReviewNotFound() {
        Long gymId = 1L;
        Long userId = 1L;

        when(reviewRepository.findByGymIdAndUserId(gymId, userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reviewService.deleteReview(gymId, userId));
    }


    /*
    댓글 수정 성공 - MoonGwangHee
     */
    @Test
    void updateComment_Success() {
        Long gymId = 1L;
        Long userId = 1L;
        Long cId = 1L;

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setComment("수정된 댓글");

        Comment savedComment = new Comment();
        savedComment.setUserId(userId);
        savedComment.setGymId(gymId);
        savedComment.setCId(cId);
        savedComment.setComment("수정된 댓글");

        when(commentRepository.findByGymIdAndUserId(gymId, userId, cId)).thenReturn(Optional.of(savedComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        Comment updatedComment = reviewService.updateComment(gymId, userId, cId, commentDTO);

        assertNotNull(updatedComment);
        assertEquals("수정된 댓글", updatedComment.getComment());
        verify(messagingTemplate).convertAndSend(eq("/topic/gym/" + gymId), anyString());
    }


    /*
    댓글 수정 실패 (댓글 찾지 못할 때)
     */
    @Test
    void updateComment_CommentNotFound() {
        Long gymId = 1L;
        Long userId = 1L;
        Long cId = 1L;
        CommentDTO commentDTO = new CommentDTO();

        when(commentRepository.findByGymIdAndUserId(gymId, userId, cId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reviewService.updateComment(gymId, userId, cId, commentDTO));

    }


    /*
    댓글 삭제 성공 - MoonGwangHee
     */
    @Test
    void deleteComment_Success() {
        Long gymId = 1L;
        Long userId = 1L;
        Long cId = 1L;

        Comment savedComment = new Comment();
        savedComment.setGymId(gymId);
        savedComment.setUserId(userId);
        savedComment.setCId(cId);

        when(commentRepository.findByGymIdAndUserId(gymId, userId, cId)).thenReturn(Optional.of(savedComment));

        assertDoesNotThrow(() -> reviewService.deleteComment(gymId, userId, cId));
        verify(commentRepository).delete(savedComment);
        verify(messagingTemplate).convertAndSend(eq("/topic/gym/" + gymId), anyString());
    }


    /*
    댓글 삭제 실패 (댓글을 찾지 못할 때)
     */
    @Test
    void deleteComment_CommentNotFound() {
        Long gymId = 1L;
        Long userId = 1L;
        Long cId = 1L;

        when(commentRepository.findByGymIdAndUserId(gymId, userId, cId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reviewService.deleteComment(gymId, userId, cId));
    }
}
