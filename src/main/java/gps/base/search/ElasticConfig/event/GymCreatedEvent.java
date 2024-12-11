package gps.base.search.ElasticConfig.event;

import gps.base.search.ElasticEntity.ElasticGym;
import lombok.Getter;

@Getter
public class GymCreatedEvent extends GymEvent {

    public GymCreatedEvent(ElasticGym elasticGym) {
        super(elasticGym);
    }
}
