package gps.base.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GymDTO {
    private Long gymId;
    private String address;
    private String gName;
    private String openHour;
    private String homepage;
    private String phone;
    private String gCreatedBy;
    private LocalDateTime gCreatedAt;
    private Double rating;

    private ImageDTO gymImage;
}
