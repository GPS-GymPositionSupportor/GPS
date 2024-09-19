package gps.base.service;


import gps.base.DTO.CommentDTO;
import gps.base.DTO.ReviewDTO;
import gps.base.model.Comment;
import gps.base.model.Review;
import gps.base.repository.CommentRepository;
import gps.base.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
}
