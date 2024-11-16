package gps.base.config.Elastic.event;

import lombok.Getter;

// 체육관 삭제 이벤트
@Getter
public class GymDeletedEvent {
    private final Long gymId;
    private final Long timestamp;

    public GymDeletedEvent(Long gymId) {
        this.gymId = gymId;
        this.timestamp = System.currentTimeMillis();
    }
}
