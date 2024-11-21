package gps.base.config.Elastic.event;

import gps.base.ElasticSearchEntity.Gym;
import lombok.Getter;

@Getter
public class GymEvent {
    protected final Gym gym;
    protected final Long timestamp;

    protected GymEvent(Gym gym) {
        this.gym = gym;
        this.timestamp = System.currentTimeMillis();
    }
}

