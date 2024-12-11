package gps.base.search.ElasticConfig.event;

import gps.base.search.ElasticEntity.ElasticGym;
import lombok.Getter;

@Getter
public class GymUpdatedEvent extends GymEvent {
    public GymUpdatedEvent(ElasticGym elasticGym) {
        super(elasticGym);
    }
}
