package gps.base.DTO;

import gps.base.model.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {
    private Long id;
    private String caption;
    private LocalDateTime addedAt;
    private String imageUrl;
    private Long userId;
    private Long gymId;
    private Long reviewId;

    // 이미지 파일 업로드를 위한 필드
    private MultipartFile file;

    // Review 와 함께 생성될 때 사용할 생성자

    public ImageDTO(String caption, LocalDateTime addedAt, Long userId, Long gymId, Long reviewId) {
        this.caption = caption;
        this.addedAt = addedAt;
        this.userId = userId;
        this.gymId = gymId;
        this.reviewId = reviewId;
    }


    // Entity를 DTO로 변환하는 정적 메소드
    public static ImageDTO fromEntity(Image image) {
        ImageDTO dto = new ImageDTO();
        dto.setId(image.getId());
        dto.setCaption(image.getCaption());
        dto.setAddedAt(image.getAddedAt());
        dto.setImageUrl(image.getImageUrl());
        dto.setUserId(image.getUserId());
        dto.setGymId(image.getGymId());
        dto.setReviewId(image.getReviewId());
        return dto;
    }


    // DTO를 Entity로 변환하는 메서드
    public Image toEntity() {
        Image image = new Image();
        image.setCaption(this.caption);
        image.setAddedAt(this.addedAt);
        image.setImageUrl(this.imageUrl);
        image.setUserId(this.userId);
        image.setGymId(this.gymId);
        image.setReviewId(this.reviewId);
        return image;
    }
}
