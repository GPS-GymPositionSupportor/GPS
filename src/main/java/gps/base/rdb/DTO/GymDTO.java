package gps.base.rdb.DTO;

import gps.base.rdb.model.Gym;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GymDTO {
    private Long gymId;
    private String address;
    private String g_name;
    private String openHour;
    private String homepage;
    private String phone;
    private String gCreatedBy;
    private LocalDateTime gCreatedAt;
    private double g_latitude;
    private double g_longitude;
    private Double rating;

    private ImageDTO gymImage;


    public static GymDTO fromEntity(Gym gym) {
        GymDTO dto = new GymDTO();
        dto.setG_name(gym.getGName());
        dto.setG_latitude(gym.getGLatitude());   // 여기서 필드명 맞추기
        dto.setG_longitude(gym.getGLongitude()); // 여기서 필드명 맞추기
        return dto;
    }
}
