package gps.base.search.ElasticConfig.event;


import gps.base.search.ElasticEntity.Gym;
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

