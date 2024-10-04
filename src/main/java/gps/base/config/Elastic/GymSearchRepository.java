package gps.base.config.Elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GymSearchRepository extends ElasticsearchRepository<GymDocument, String> {
}