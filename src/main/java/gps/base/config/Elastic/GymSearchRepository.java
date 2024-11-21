package gps.base.config.Elastic;

import gps.base.config.Elastic.Document.GymDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GymSearchRepository extends ElasticsearchRepository<GymDocument, String> {
    List<GymDocument> findByNameContaining(String name);

    @Query("{\"bool\":{\"filter\":{\"geo_distance\":{\"distance\":\"?0km\",\"location\":{\"lat\":?1,\"lon\":?2}}}}}")
    List<GymDocument> searchByLocation(double distance, double lat, double lon);

    List<GymDocument> findByLocationNear(double distanceKm, double lat, double lon);
}