package gps.base.search.ElasticConfig.event;

import gps.base.search.ElasticEntity.Gym;
import lombok.Getter;

@Getter
public class GymUpdatedEvent extends GymEvent {
    public GymUpdatedEvent(Gym gym) {
        super(gym);
    }
}
