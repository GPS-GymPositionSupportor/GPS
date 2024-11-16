package gps.base.config.Elastic.event;

import gps.base.ElasticSearchEntity.Gym;
import lombok.Getter;

// 체육관 생성 이벤트
@Getter
public class GymCreatedEvent extends GymEvent {
    public GymCreatedEvent(Gym gym) {
        super(gym);
    }
}
