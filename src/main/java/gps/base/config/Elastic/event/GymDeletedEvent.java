package gps.base.config.Elastic.event;

import lombok.Getter;

@Getter
public class GymDeletedEvent {
    private final Long gymId;

    public GymDeletedEvent(Long gymId) {
        this.gymId = gymId;
    }
}
