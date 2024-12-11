package gps.base.search.ElasticConfig.event;

import gps.base.search.ElasticEntity.Gym;
import lombok.Getter;

@Getter
public class GymCreatedEvent extends GymEvent {

    public GymCreatedEvent(Gym gym) {
        super(gym);
    }
}
