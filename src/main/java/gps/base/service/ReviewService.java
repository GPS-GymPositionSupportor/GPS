package gps.base.service;

import gps.base.DTO.CommentDTO;
import gps.base.DTO.ReviewDTO;
import gps.base.error.ErrorCode;
import gps.base.error.exception.CustomException;
import gps.base.model.*;
import gps.base.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Value("${upload.path}")
    private String uploadPath;
    
    
    /*
    리뷰 관련
     */

    // 리뷰 작성
    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO, MultipartFile file) throws IOException {
        // 체육관 존재 여부 확인
        if (!gymRepository.existsById(reviewDTO.getGymId())) {
            throw new CustomException(ErrorCode.GYM_NOT_FOUND);
        }

        try {
            // Review 엔티티 생성 및 저장
            Review review = new Review();
            review.setUserId(reviewDTO.getUserId());
            review.setGymId(reviewDTO.getGymId());
            review.setComment(reviewDTO.getComment());
            review.setAddedAt(LocalDateTime.now());

            Review savedReview = reviewRepository.save(review);

            // 이미지 파일이 있는 경우 처리
            if (file != null && !file.isEmpty()) {
                String fileName = saveImage(file);  // 이미지 파일 저장
                saveImageEntity(fileName, file.getOriginalFilename(), savedReview, reviewDTO.getUserId());  // 이미지 정보 DB 저장
            }

            // 웹소켓 알림
            messagingTemplate.convertAndSend("/topic/gym/" + review.getGymId(), "새로운 리뷰가 작성되었습니다.");

            return convertToDTO(savedReview);

        } catch (Exception e) {
            log.error("리뷰 생성 중 오류 발생: ", e);
            throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

    // 이미지 타입 검증
    private boolean isValidImageType(String contentType) {
        return contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif") ||
                contentType.equals("image/webp");
    }


    @Transactional
    protected void saveImageForReview(MultipartFile file, Review savedReview, Long userId) throws IOException {
        String fileName = null;
        try {
            // UUID와 원본 파일명으로 새 파일명 생성
            fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // 파일 저장
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Files.copy(file.getInputStream(), uploadDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

            // Image 엔티티 생성 시 fileName만 저장 (중복 방지)
            Image image = Image.builder()
                    .imageUrl(fileName)  // fileName만 저장, 중복된 경로 제거
                    .userId(userId)
                    .gymId(savedReview.getGymId())
                    .reviewId(savedReview.getRId())
                    .caption(file.getOriginalFilename())
                    .addedAt(LocalDateTime.now())
                    .build();

            imageRepository.save(image);
        } catch (IOException e) {
            log.error("Failed to save image: {}", e.getMessage());
            if (fileName != null) {
                try {
                    Files.deleteIfExists(Paths.get(uploadPath).resolve(fileName));
                } catch (IOException ex) {
                    log.error("Failed to delete file after error: {}", ex.getMessage());
                }
            }
            throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }


    // 이미지 저장
    private String saveImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path uploadDir = Paths.get(uploadPath);

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        Path filePath = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        log.info("Saved image file: {}", fileName);  // 로깅 추가
        return fileName;
    }

    // 이미지 엔티티 저장
    private void saveImageEntity(String fileName, String originalFileName, Review review, Long userId) {
        Image image = Image.builder()
                .imageUrl(fileName)  // 생성된 파일명만 저장
                .userId(userId)
                .gymId(review.getGymId())
                .reviewId(review.getRId())
                .caption(originalFileName)
                .addedAt(LocalDateTime.now())
                .build();

        imageRepository.save(image);
        log.debug("Saved image entity with URL: {}", fileName);  // 로깅 추가
    }


    // 이미지 삭제
    private void deleteImageFile(String filename) throws IOException {
        Path filePath = Paths.get(uploadPath).resolve(filename);
        Files.deleteIfExists(filePath);
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setRId(review.getRId());
        dto.setUserId(review.getUserId());
        dto.setGymId(review.getGymId());
        dto.setComment(review.getComment());
        dto.setAddedAt(review.getAddedAt());

        // userId를 이용해서 Member 정보를 조회하고 이름을 설정
        memberRepository.findById(review.getUserId())
                .ifPresent(member -> dto.setUserName(member.getName()));

        // 리뷰에 연결된 이미지 정보 조회
        List<Image> images = imageRepository.findByReviewId(review.getRId());
        if(!images.isEmpty()) {
            Image image = images.get(0); // 첫 번째 이미지 정보만 사용
            log.debug("Image URL being set: {}", image.getImageUrl());
            dto.setReviewImage(image.getImageUrl());
            dto.setCaption(image.getCaption());
        }

        return dto;
    }

    // Gym_id 값으로 리뷰 가져오기
    public List<Review> getReviewsByGym(Long gymId) {
        return reviewRepository.findByGymId(gymId);
    }

    // 리뷰 ID로 단일 리뷰 가져오기
    public Review getReviewById(Long rId, Long gymId, Long userId) {
        return reviewRepository.findByrIdAndGymIdAndUserId(rId, gymId, userId)
                .orElseThrow(() -> new EntityNotFoundException(rId + " 에 해당하는 리뷰는 존재하지 않습니다."));
    }

    // 모든 리뷰 가져오기
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }


    // Review와 Member 정보 함께 가져오기
    public List<ReviewDTO> getAllReviewsWithUserName() {
        List<ReviewDTO> reviews = reviewRepository.findAllReviewsWithUserName();

        for(ReviewDTO review : reviews) {
            List<Image> images = imageRepository.findByReviewId(review.getRId());
            if(!images.isEmpty()) {
                Image image = images.get(0);
                // DB에 저장된 파일명만 사용
                review.setReviewImage(image.getImageUrl());  // 예: "123e4567-e89b-12d3-a456-426614174000_image.jpg"
            }
        }
        return reviews;
    }

    private String extractFileName(String fullPath) {
        if (fullPath == null) return null;
        try {
            return Paths.get(fullPath).getFileName().toString();
        } catch (Exception e) {
            log.error("Failed to extract filename from path: {}", fullPath, e);
            return fullPath;
        }
    }

    
    
    // 리뷰 수정
    @Transactional
    public Review updateReview(Long rId , Long gymId, Long userId, ReviewDTO reviewDTO) {
        Review review = validateReviewAndImage(rId, gymId, userId, null);  // 검증 재사용
        review.setComment(reviewDTO.getComment());
        // 추후 다른 필드들도 업데이트가 필요하면 코드추가

        Review updatedReview = reviewRepository.save(review);
        messagingTemplate.convertAndSend("/topic/gym/" + gymId, "리뷰가 수정되었습니다.");
        return updatedReview;
    }


    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long rId, Long gymId, Long userId) {
        Review review = validateReviewAndImage(rId, gymId, userId, null);   // 검증 재사용
        
        // 연관된 이미지 삭제
        imageRepository.deleteByReviewId(rId);
        
        // 리뷰 삭제
        reviewRepository.delete(review);
        messagingTemplate.convertAndSend("/topic/gym/" + gymId, "리뷰가 삭제되었습니다.");
    }

    // 리뷰와 댓글 검증
    public void validateReviewAndComment(Long reviewId, Long commentId, Long userId) {
        // 리뷰 존재 여부 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        // 댓글 존재 여부와 작성자 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        
        // 댓글이 해당 리뷰의 댓글이 맞는지 확인
        if (!comment.getReview().getRId().equals(reviewId)) {
            throw new CustomException(ErrorCode.REVIEW_NOT_FOUND);
        }
        
        // 댓글 작성자 확인
        if(!comment.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.WRITER_DOES_NOT_MATCH);
        }
    }

    // 리뷰만 검증
    public Review validateReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        // 리뷰 작성자 확인
        if(!review.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.WRITER_DOES_NOT_MATCH);
        }

        return review;
    }

    // 리뷰 & 이미지 검증
    public Review validateReviewAndImage(Long reviewId, Long gymId, Long userId, MultipartFile file) {
        // 체육관 존재 여부 확인
        if (!gymRepository.existsById(gymId)) {
            throw new CustomException(ErrorCode.GYM_NOT_FOUND);
        }

        // 리뷰 존재 여부와 작성자 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.WRITER_DOES_NOT_MATCH);
        }

        // 파일이 있는 경우 이미지 검증
        if (file != null && !file.isEmpty()) {
            validateImageFile(file);
        }

        return review;
    }

    // 이미지 파일 검증 메서드
    private void validateImageFile(MultipartFile file) {
        // 파일이 비어있는지 확인
        if (file.isEmpty()) {
            throw new CustomException(ErrorCode.NO_FILE_PROVIDED);
        }

        // 파일 형식 검증
        String contentType = file.getContentType();
        if (contentType == null || !isValidImageType(contentType)) {
            throw new CustomException(ErrorCode.NOT_IMAGE_FILE);
        }

        // 파일 크기 검증 (예: 5MB)
        long maxSize = 5 * 1024 * 1024; // 5MB
        if (file.getSize() > maxSize) {
            throw new CustomException(ErrorCode.FILE_SIZE_EXCEED);
        }
    }

    
    /*
    댓글 관련
     */


    // 댓글 작성
    public Comment addComment(Long rId, Long userId,  CommentDTO commentDTO) {
        Review review = reviewRepository.findById(rId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 리뷰가 존재하지 않습니다."));

        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setReview(review);
        comment.setComment(commentDTO.getComment());
        Comment savedComment = commentRepository.save(comment);

        messagingTemplate.convertAndSend("/topic/gym/" + review.getGymId(), "새로운 댓글이 작성되었습니다.");
        return savedComment;

    }

    // 리뷰에 대한 댓글 가져오기
    public List<Comment> getCommentsByReview(Long rId) {
        Review review = reviewRepository.findById(rId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 리뷰가 존재하지 않습니다."));
        return commentRepository.findByReview(review);
    }
    
    
    
    // 댓글 수정
    @Transactional
    public Comment updateComment(Long rId, Long commentId, Long userId, CommentDTO commentDTO) {
        Review review = reviewRepository.findById(rId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 리뷰가 존재하지 않습니다."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 댓글이 존재하지 않습니다."));

        if(!comment.getUserId().equals(userId) || !comment.getReview().equals(rId)) {
            throw new IllegalArgumentException("댓글을 수정할 권한이 없습니다.");
        }

        comment.setComment(commentDTO.getComment());
        Comment updatedComment = commentRepository.save(comment);
        messagingTemplate.convertAndSend("/topic/gym/" + review.getGymId(), "댓글이 수정되었습니다.");
        return updatedComment;
    }



    // 댓글 삭제
    @Transactional
    public void deleteComment(Long rId, Long commentId, Long userId) {
        Review review = reviewRepository.findById(rId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 리뷰가 존재하지 않습니다."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 댓글이 존재하지 않습니다."));

        if(!comment.getUserId().equals(userId) || !comment.getReview().equals(rId)) {
            throw new IllegalArgumentException("댓글을 삭제할 권한이 없습니다.");
        }

        commentRepository.delete(comment);
        messagingTemplate.convertAndSend("/topic/gym/" + review.getGymId(), "댓글이 삭제되었습니다.");
    }

    // gym_id 값에 의해서 gym 데이터 요청
    public List<Comment> getCommentsByGym(Long gymId) {
        List<Review> reviews = getReviewsByGym(gymId);
        List<Comment> comments = new ArrayList<>();
        for (Review review : reviews) {
            comments.addAll(commentRepository.findByReview(review));
        }

        return comments;
    }

}
