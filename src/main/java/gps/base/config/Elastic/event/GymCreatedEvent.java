package gps.base.config.Elastic.event;

import gps.base.ElasticSearchEntity.Gym;
import lombok.Getter;

@Getter
public class GymCreatedEvent extends GymEvent {
    public GymCreatedEvent(Gym gym) {
        super(gym);
    }
}
