package gps.base.search.ElasticRepository;

import gps.base.search.ElasticConfig.Document.GymDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GymSearchRepository extends ElasticsearchRepository<GymDocument, String> {
    List<GymDocument> findByNameContaining(String name);

    @Query("{\"bool\":{\"filter\":{\"geo_distance\":{\"distance\":\"?0km\",\"location\":{\"lat\":?1,\"lon\":?2}}}}}")
    List<GymDocument> searchByLocation(double distance, double lat, double lon);

    List<GymDocument> findByLocationNear(double distanceKm, double lat, double lon);

    @Query("""
        {
            "bool": {
                "should": [
                    {
                        "match": {
                            "name": {
                                "query": "?0",
                                "operator": "OR",
                                "fuzziness": "AUTO"
                            }
                        }
                    },
                    {
                        "match": {
                            "address1": {
                                "query": "?0",
                                "operator": "OR",
                                "fuzziness": "AUTO"
                            }
                        }
                    }
                ],
                "minimum_should_match": 1
            }
        }
    """)
    List<GymDocument> searchByKeyword(String keyword);

    // 카테고리와 키워드로 검색
    @Query("""
        {
            "bool": {
                "must": [
                    {
                        "bool": {
                            "should": [
                                { "match": { "name": { "query": "?0", "boost": 2.0 } } },
                                { "match": { "address1": { "query": "?0" } } }
                            ],
                            "minimum_should_match": 1
                        }
                    },
                    { "term": { "category": "?1" } }
                ]
            }
        }
    """)
    List<GymDocument> searchByKeywordAndCategory(String keyword, String category);
}