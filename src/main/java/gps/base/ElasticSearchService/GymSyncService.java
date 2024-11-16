package gps.base.ElasticSearchService;

import gps.base.config.Elastic.Document.*;
import gps.base.ElasticSearchEntity.Gym;
import gps.base.config.Elastic.event.*;
import gps.base.repository.GymRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GymSyncService {
    private final ElasticsearchOperations elasticsearchOperations;
    private final GymRepository gymRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void syncAllGymsOnStartup() {
        try {
            List<Gym> allGyms = gymRepository.findAll();
            List<GymDocument> documents = allGyms.stream()
                    .map(this::convertToDocument)
                    .collect(Collectors.toList());

            elasticsearchOperations.indexOps(GymDocument.class).delete();
            elasticsearchOperations.indexOps(GymDocument.class).create();

            documents.forEach(doc -> {
                try {
                    elasticsearchOperations.save(doc);
                } catch (Exception e) {
                    log.error("Error saving gym document: " + doc.getId(), e);
                }
            });

            log.info("Successfully synced {} gyms to Elasticsearch", documents.size());
        } catch (Exception e) {
            log.error("Failed to sync gyms to Elasticsearch", e);
            throw new RuntimeException("데이터 동기화 중 오류가 발생했습니다.", e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGymCreated(GymCreatedEvent event) {
        try {
            syncGymToElasticsearch(event.getGym());
            log.info("Gym created and synced: {}", event.getGym().getId());
        } catch (Exception e) {
            log.error("Error handling gym creation: " + event.getGym().getId(), e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGymUpdated(GymUpdatedEvent event) {
        try {
            syncGymToElasticsearch(event.getGym());
            log.info("Gym updated and synced: {}", event.getGym().getId());
        } catch (Exception e) {
            log.error("Error handling gym update: " + event.getGym().getId(), e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGymDeleted(GymDeletedEvent event) {
        try {
            elasticsearchOperations.delete(
                    event.getGymId().toString(),
                    GymDocument.class
            );
            log.info("Gym deleted from Elasticsearch: {}", event.getGymId());
        } catch (Exception e) {
            log.error("Error handling gym deletion: " + event.getGymId(), e);
        }
    }

    private void syncGymToElasticsearch(Gym gym) {
        GymDocument document = convertToDocument(gym);
        elasticsearchOperations.save(document);
    }

    private GymDocument convertToDocument(Gym gym) {
        if (gym == null) {
            log.warn("Gym entity is null");
            return null;
        }

        return GymDocument.builder()
                .id(gym.getId().toString())
                .name(gym.getName())
                .category(gym.getCategory())
                .address(gym.getAddress1())
                .location(new GeoPoint(
                        gym.getLatitude() != null ? gym.getLatitude() : 0.0,
                        gym.getLongitude() != null ? gym.getLongitude() : 0.0
                ))
                .rating(gym.getRating() != null ? gym.getRating() : 0.0)
                .openingHours(gym.getOpeningHours())
                .phoneNumber(gym.getPhoneNumber())
                .build();
    }
}