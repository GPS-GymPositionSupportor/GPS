package gps.base.config.Elastic.event;

import gps.base.ElasticSearchEntity.Gym;
import lombok.Getter;

// 기본 이벤트 클래스
@Getter
public abstract class GymEvent {
    protected final Gym gym;
    protected final Long timestamp;

    protected GymEvent(Gym gym) {
        this.gym = gym;
        this.timestamp = System.currentTimeMillis();
    }
}

