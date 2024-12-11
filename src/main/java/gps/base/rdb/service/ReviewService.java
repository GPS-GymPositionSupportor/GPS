package gps.base.rdb.service;

import gps.base.rdb.DTO.CommentDTO;
import gps.base.rdb.DTO.ReviewDTO;
import gps.base.rdb.error.ErrorCode;
import gps.base.rdb.error.exception.CustomException;
import gps.base.rdb.model.*;
import gps.base.rdb.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;


    private Authority authority;

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

    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    @Value("${RESOURCE_PATH}")
    private String resourcePath;


    
    /*
    리뷰 관련
     */

    // 리뷰 작성
    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO, List<MultipartFile> files) throws IOException {
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
            if (files != null && !files.isEmpty()) {
                saveImageForReview(files, savedReview, reviewDTO.getUserId());  // 파라미터 수정
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
    protected void saveImageForReview(List<MultipartFile> files, Review savedReview, Long userId) {
        for (MultipartFile file : files) {
            try {
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

                // 파일 저장
                Path uploadDir = Paths.get(resourcePath);
                Path targetPath = uploadDir.resolve(fileName);
                Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                log.info("파일 저장 : {}", targetPath);

                // Image 엔티티 생성 및 저장
                Image image = Image.builder()
                        .imageUrl(fileName)
                        .userId(userId)
                        .gymId(savedReview.getGymId())
                        .reviewId(savedReview.getRId())
                        .caption(file.getOriginalFilename())
                        .addedAt(LocalDateTime.now())
                        .build();

                imageRepository.save(image);
                log.debug("URL과 저장된 이미지 엔티티를 저장하였습니다 : {}", fileName);
            } catch (IOException e) {
                throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAILED);
            }
        }
    }


    // 이미지 저장
    private String saveImage(MultipartFile file) throws CustomException {
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path uploadDir = Paths.get(resourcePath);
            Path targetPath = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("File saved to: {}", targetPath);
            return fileName;
        } catch (IOException e) {
            throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
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

        memberRepository.findById(review.getUserId())
                .ifPresent(member -> dto.setUserName(member.getName()));




        List<Image> images = imageRepository.findByReviewId(review.getRId());
        log.info("Found {} images", images.size());

        if(!images.isEmpty()) {
            List<String> imageUrls = images.stream()
                    .map(Image::getImageUrl)
                    .collect(Collectors.toList());

            log.info("Review {} has {} images: {}", review.getRId(), imageUrls.size(), imageUrls);

            // 단일 이미지용
            dto.setReviewImage(images.get(0).getImageUrl());

            // 다중 이미지 설정
            dto.setReviewImages(imageUrls);  // 여기서 실제로 설정되는지 확인

            log.info("After setting images to DTO: {}", dto.getReviewImages());
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
            // Member 정보 가져오기
            Member member = memberRepository.findById(review.getUserId())
                    .orElseThrow(() -> new RuntimeException("Member not found"));
            review.setUserName(member.getName());  // Member의 name을 DTO에 설정

            List<Image> images = imageRepository.findByReviewId(review.getRId());
            if(!images.isEmpty()) {
                // 단일 이미지 설정 (하위 호환성)
                review.setReviewImage(images.get(0).getImageUrl());

                // 모든 이미지 URL을 리스트로 설정
                List<String> imageUrls = images.stream()
                        .map(Image::getImageUrl)
                        .collect(Collectors.toList());
                review.setReviewImages(imageUrls);

                log.info("Review {} setting multiple images: {}", review.getRId(), imageUrls);
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
    public Review updateReview(Long rId , Long gymId, Long userId, ReviewDTO reviewDTO, Authority authority) {
        Review review = validateReviewAndImage(rId, gymId, userId, null, authority);  // 검증 재사용
        review.setComment(reviewDTO.getComment());
        // 추후 다른 필드들도 업데이트가 필요하면 코드추가

        Review updatedReview = reviewRepository.save(review);
        messagingTemplate.convertAndSend("/topic/gym/" + gymId, "리뷰가 수정되었습니다.");
        return updatedReview;
    }


    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long rId, Long gymId, Long userId, Authority authority) {
        Review review = validateReviewAndImage(rId, gymId, userId, null, authority);   // 검증 재사용
        
        // 연관된 이미지 삭제
        imageRepository.deleteByReviewId(rId);
        
        // 리뷰 삭제
        reviewRepository.delete(review);
        messagingTemplate.convertAndSend("/topic/gym/" + gymId, "리뷰가 삭제되었습니다.");
    }

    // 리뷰와 댓글 검증
    public void validateReviewAndComment(Long reviewId, Long commentId, Long userId, Authority authority) {
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

        // 관리자이거나 작성자일 경우 삭제 가능
        if (authority != Authority.ADMIN && !comment.getUserId().equals(userId)) {
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
    public Review validateReviewAndImage(Long reviewId, Long gymId, Long userId, MultipartFile file, Authority authority) {
        // 체육관 존재 여부 확인
        if (!gymRepository.existsById(gymId)) {
            throw new CustomException(ErrorCode.GYM_NOT_FOUND);
        }

        // 리뷰 존재 여부와 작성자 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        // 관리자인 경우, 작성자와 관계 없이 삭제 가능
        if (authority != Authority.ADMIN && !review.getUserId().equals(userId)) {
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

    public Page<ReviewDTO> getAllReviewsWithPaging(Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findAll(pageable);
        return reviewPage.map(review -> {
            ReviewDTO dto = convertToDTO(review); // 기존 변환 메서드 사용

            List<Image> images = imageRepository.findByReviewId(review.getRId());
            if(!images.isEmpty()) {
                dto.setReviewImage(images.get(0).getImageUrl());
                List<String> imageUrls = images.stream()
                        .map(Image::getImageUrl)
                        .collect(Collectors.toList());
                dto.setReviewImages(imageUrls);
            }

            return dto;
        });
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
    public void deleteComment(Long rId, Long commentId, Long userId, Authority authority) {
        Review review = reviewRepository.findById(rId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 리뷰가 존재하지 않습니다."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 댓글이 존재하지 않습니다."));

        // 관리자이거나 댓글 작성자인 경우에만 삭제 가능
        if (authority != Authority.ADMIN && (!comment.getUserId().equals(userId) || !comment.getReview().equals(rId))) {
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

    public List<Review> getReviewsByGymId(Long gymId) {
        return reviewRepository.findByGymId(gymId);
    }

    // 새로운 메서드 추가 (Map 반환)
    public List<Map<String, Object>> getReviewWithMember(Long gymId) {
        List<Review> reviews = reviewRepository.findByGymId(gymId);
        return reviews.stream()
                .map(this::convertReviewToMap)
                .collect(Collectors.toList());

    }

    public Map<String, Object> convertReviewToMap(Review review) {
        Map<String, Object> reviewMap = new HashMap<>();
        reviewMap.put("reviewId", review.getRId());
        reviewMap.put("content", review.getComment());
        reviewMap.put("createdAt", review.getAddedAt());

        reviewMap.put("commentCount", review.getComments().size());

        // Member 정보 조회 및 추가
        Member member = memberRepository.findById(review.getUserId())
                .orElse(null);
        if (member != null) {
            reviewMap.put("userProfileImage", member.getProfileImg());
            reviewMap.put("userName", member.getNickname());
        }


        return reviewMap;
    }

    public Page<ReviewDTO> getReviewsByUserId(Long userId, PageRequest pageRequest) {
        Page<Review> reviews = reviewRepository.findByUserIdOrderByAddedAtDesc(userId, pageRequest);

        return reviews.map(review -> {
            // DTO를 나중에 생성
            ReviewDTO dto = new ReviewDTO();

            // 기본 정보 설정
            dto.setRId(review.getRId());
            dto.setAddedAt(review.getAddedAt());
            dto.setUserId(review.getUserId());
            dto.setComment(review.getComment());

            // Member 정보 설정
            memberRepository.findById(review.getUserId())
                    .ifPresent(member -> {
                        dto.setUserName(member.getName());
                        // 필요한 다른 Member 정보들도 여기서 설정
                    });

            return dto;
        });
    }
}