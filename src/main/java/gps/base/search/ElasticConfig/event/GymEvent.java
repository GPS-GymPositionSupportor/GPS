package gps.base.search.ElasticConfig.event;


import gps.base.search.ElasticEntity.ElasticGym;
import lombok.Getter;

@Getter
public class GymEvent {
    protected final ElasticGym elasticGym;
    protected final Long timestamp;

    protected GymEvent(ElasticGym elasticGym) {
        this.elasticGym = elasticGym;
        this.timestamp = System.currentTimeMillis();
    }
}

