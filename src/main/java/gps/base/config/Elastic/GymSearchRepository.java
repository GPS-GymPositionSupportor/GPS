package gps.base.config.Elastic;

import gps.base.config.Elastic.Document.GymDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GymSearchRepository extends ElasticsearchRepository<GymDocument, String> {
    List<GymDocument> findByNameContaining(String name);
}