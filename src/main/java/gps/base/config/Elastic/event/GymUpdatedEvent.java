package gps.base.config.Elastic.event;

import gps.base.ElasticSearchEntity.Gym;
import lombok.Getter;

// 체육관 수정 이벤트
@Getter
public class GymUpdatedEvent extends GymEvent {
    private final Gym previousGym;

    public GymUpdatedEvent(Gym gym, Gym previousGym) {
        super(gym);
        this.previousGym = previousGym;
    }
}
