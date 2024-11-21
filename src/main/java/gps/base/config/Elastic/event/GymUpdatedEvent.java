package gps.base.config.Elastic.event;

import gps.base.ElasticSearchEntity.Gym;
import lombok.Getter;

@Getter
public class GymUpdatedEvent extends GymEvent {
    public GymUpdatedEvent(Gym gym) {
        super(gym);
    }
}
