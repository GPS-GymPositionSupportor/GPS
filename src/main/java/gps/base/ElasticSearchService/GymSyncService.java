package gps.base.ElasticSearchService;

import gps.base.config.Elastic.Document.*;
import gps.base.ElasticSearchEntity.Gym;
import gps.base.config.Elastic.event.*;
import gps.base.repository.GymRepository;
import gps.base.repository.ReviewRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GymSyncService {
    private final ElasticsearchOperations elasticsearchOperations;
    private final GymRepository gymRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 애플리케이션 시작 시 전체 동기화 수행
     */



    @EventListener(ApplicationReadyEvent.class)
    public void syncAllGymsOnStartup() {
        try {
            log.info("Starting initial sync at application startup");
            fullSync();
            log.info("Initial sync completed successfully");
        } catch (Exception e) {
            log.error("Initial sync failed", e);
            throw new RuntimeException("초기 동기화 실패", e);
        }
    }

    /**
     * 매시간 정기적인 동기화 수행
     * cron = "0 0 * * * *" 설명:
     * 초(0) 분(0) 시간(*) 일(*) 월(*) 요일(*)
     */
    @Scheduled(cron = "0 0 * * * *")
    public void scheduledSync() {
        try {
            log.info("Starting scheduled sync at {}", LocalDateTime.now());
            fullSync();
            log.info("Scheduled sync completed successfully");
        } catch (Exception e) {
            log.error("Scheduled sync failed: {}", e.getMessage(), e);
            // 운영팀에 알림 발송 로직 추가 가능
        }
    }

    /**
     * 전체 데이터 동기화 수행
     * 1. 기존 인덱스 삭제
     * 2. 새 인덱스 생성
     * 3. 전체 데이터 재색인
     */
    private void fullSync() {
        log.info("Starting full sync process");

        // 1. DB에서 1000개 데이터만 조회
        List<Gym> allGyms = gymRepository.findAll(PageRequest.of(0, 1000)).getContent();
        log.info("Found {} gyms in database", allGyms.size());

        // 2. Document 변환
        List<GymDocument> documents = allGyms.stream()
                .map(this::convertToDocument)
                .filter(doc -> doc != null)
                .collect(Collectors.toList());

        try {
            // 3. 기존 인덱스 삭제
            elasticsearchOperations.indexOps(GymDocument.class).delete();
            log.info("Deleted existing index");

            // 4. 새 인덱스 생성
            elasticsearchOperations.indexOps(GymDocument.class).create();
            log.info("Created new index");

            // 5. 벌크 저장 수행
            documents.forEach(doc -> {
                try {
                    elasticsearchOperations.save(doc);
                } catch (Exception e) {
                    log.error("Error saving gym document: {} - {}", doc.getId(), e.getMessage());
                }
            });

            log.info("Successfully synced {} gyms to Elasticsearch", documents.size());
        } catch (Exception e) {
            log.error("Error during full sync: {}", e.getMessage(), e);
            throw new RuntimeException("전체 동기화 중 오류 발생", e);
        }
    }

    /**
     * 체육관 생성 이벤트 처리
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGymCreated(GymCreatedEvent event) {
        try {
            log.info("Handling gym creation event for gym ID: {}", event.getGym().getId());
            syncGymToElasticsearch(event.getGym());
            log.info("Successfully synced new gym: {}", event.getGym().getId());
        } catch (Exception e) {
            log.error("Error handling gym creation: {} - {}", event.getGym().getId(), e.getMessage(), e);
        }
    }

    /**
     * 체육관 수정 이벤트 처리
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGymUpdated(GymUpdatedEvent event) {
        try {
            log.info("Handling gym update event for gym ID: {}", event.getGym().getId());
            syncGymToElasticsearch(event.getGym());
            log.info("Successfully synced updated gym: {}", event.getGym().getId());
        } catch (Exception e) {
            log.error("Error handling gym update: {} - {}", event.getGym().getId(), e.getMessage(), e);
        }
    }

    /**
     * 체육관 삭제 이벤트 처리
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGymDeleted(GymDeletedEvent event) {
        try {
            log.info("Handling gym deletion event for gym ID: {}", event.getGymId());
            elasticsearchOperations.delete(
                    event.getGymId().toString(),
                    GymDocument.class
            );
            log.info("Successfully deleted gym from Elasticsearch: {}", event.getGymId());
        } catch (Exception e) {
            log.error("Error handling gym deletion: {} - {}", event.getGymId(), e.getMessage(), e);
        }
    }

    /**
     * 단일 체육관 동기화
     */
    private void syncGymToElasticsearch(Gym gym) {
        try {
            GymDocument document = convertToDocument(gym);
            if (document != null) {
                elasticsearchOperations.save(document);
                log.debug("Synced gym {} to Elasticsearch", gym.getId());
            }
        } catch (Exception e) {
            log.error("Error syncing gym {}: {}", gym.getId(), e.getMessage(), e);
            throw new RuntimeException("체육관 동기화 실패", e);
        }
    }

    /**
     * Gym 엔티티를 GymDocument로 변환
     */
    private GymDocument convertToDocument(Gym gym) {
        if (gym == null) return null;

        try {
            return GymDocument.builder()
                    .id(gym.getId().toString())
                    .name(gym.getName())
                    .location(new GeoPoint(
                            gym.getLatitude() != null ? gym.getLatitude() : 0.0,
                            gym.getLongitude() != null ? gym.getLongitude() : 0.0
                    ))
                    .rating(gym.getRating() != null ? gym.getRating() : 0.0)
                    .build();
        } catch (Exception e) {
            log.error("Error converting gym {} to document: {}", gym.getId(), e.getMessage(), e);
            return null;
        }
    }
}
