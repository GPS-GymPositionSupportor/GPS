package gps.base.search.ElasticService;

import gps.base.search.ElasticConfig.Document.GymDocument;
import gps.base.search.ElasticConfig.event.GymCreatedEvent;
import gps.base.search.ElasticConfig.event.GymDeletedEvent;
import gps.base.search.ElasticConfig.event.GymUpdatedEvent;
import gps.base.search.ElasticEntity.ElasticGym;
import gps.base.search.ElasticRepository.ElasticGymRepository;
import gps.base.search.ElasticRepository.GymSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GymSyncService {
    private final GymSearchRepository gymSearchRepository;
    private final ElasticGymRepository elasticGymRepository;

    private static final int SYNC_LIMIT = 500;

    /**
     * 애플리케이션 시작 시 동기화 (500개 제한)
     */
    @EventListener(ApplicationReadyEvent.class)
    public void syncAllGymsOnStartup() {
        try {
            log.info("Starting initial sync of gyms (limit: {})", SYNC_LIMIT);

            // 기존 인덱스 데이터 전체 삭제
            gymSearchRepository.deleteAll();

            // 상위 500개 체육관 정보 가져오기
            List<ElasticGym> gymsToSync = elasticGymRepository.findAll(PageRequest.of(0, SYNC_LIMIT)).getContent();
            log.info("Found {} gyms to sync", gymsToSync.size());

            // ElasticSearch에 동기화
            gymsToSync.forEach(this::syncGymToElasticsearch);

            log.info("Successfully completed initial sync of {} gyms", gymsToSync.size());
        } catch (Exception e) {
            log.error("Failed to perform initial sync: ", e);
            throw new RuntimeException("Initial sync failed", e);
        }
    }

    /**
     * 1시간마다 전체 동기화 수행 (500개 제한)
     */
    @Scheduled(cron = "0 0 * * * *")
    public void scheduledSync() {
        try {
            log.info("Starting scheduled sync (limit: {})", SYNC_LIMIT);
            List<ElasticGym> gymsToSync = elasticGymRepository.findAll(PageRequest.of(0, SYNC_LIMIT)).getContent();
            gymsToSync.forEach(this::syncGymToElasticsearch);
            log.info("Successfully completed scheduled sync of {} gyms", gymsToSync.size());
        } catch (Exception e) {
            log.error("Failed to perform scheduled sync: ", e);
        }
    }

    /**
     * 체육관 생성 이벤트 처리
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGymCreated(GymCreatedEvent event) {
        try {
            // 현재 인덱스된 문서 수 확인
            long currentCount = gymSearchRepository.count();

            if (currentCount < SYNC_LIMIT) {
                log.info("Handling gym creation event for gym ID: {}", event.getElasticGym().getId());
                syncGymToElasticsearch(event.getElasticGym());
            } else {
                log.warn("Sync limit reached ({}). Skipping gym creation sync for ID: {}",
                        SYNC_LIMIT, event.getElasticGym().getId());
            }
        } catch (Exception e) {
            log.error("Failed to handle gym creation event: ", e);
        }
    }

    /**
     * 체육관 수정 이벤트 처리
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGymUpdated(GymUpdatedEvent event) {
        try {
            // 이미 인덱스에 있는 문서만 업데이트
            if (gymSearchRepository.existsById(event.getElasticGym().getId().toString())) {
                log.info("Handling gym update event for gym ID: {}", event.getElasticGym().getId());
                syncGymToElasticsearch(event.getElasticGym());
            }
        } catch (Exception e) {
            log.error("Failed to handle gym update event: ", e);
        }
    }

    /**
     * 체육관 삭제 이벤트 처리
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGymDeleted(GymDeletedEvent event) {
        try {
            if (gymSearchRepository.existsById(event.getGymId().toString())) {
                log.info("Handling gym deletion event for gym ID: {}", event.getGymId());
                gymSearchRepository.deleteById(event.getGymId().toString());
            }
        } catch (Exception e) {
            log.error("Failed to handle gym deletion event: ", e);
        }
    }

    /**
     * 개별 체육관 동기화
     */
    private void syncGymToElasticsearch(ElasticGym elasticGym) {
        try {
            GymDocument document = convertToDocument(elasticGym);
            gymSearchRepository.save(document);
            log.debug("Successfully synced gym: {}", elasticGym.getId());
        } catch (Exception e) {
            log.error("Failed to sync gym {}: ", elasticGym.getId(), e);
        }
    }

    /**
     * Gym 엔티티를 GymDocument로 변환
     */
    private GymDocument convertToDocument(ElasticGym elasticGym) {
        if (elasticGym == null) {
            throw new IllegalArgumentException("Gym cannot be null");
        }

        return GymDocument.builder()
                .id(elasticGym.getId().toString())
                .name(elasticGym.getName())
                .latitude(elasticGym.getLatitude())
                .longitude(elasticGym.getLongitude())
                .location(new GeoPoint(elasticGym.getLatitude(), elasticGym.getLongitude()))
                .build();
    }
}