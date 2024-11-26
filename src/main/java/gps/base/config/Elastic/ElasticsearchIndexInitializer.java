package gps.base.config.Elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.StringReader;

@Slf4j
@Component
@RequiredArgsConstructor
public class ElasticsearchIndexInitializer {
    private final ElasticsearchClient elasticsearchClient;
    private static final String INDEX_NAME = "gyms";

    @PostConstruct
    public void init() {
        try {
            boolean exists = elasticsearchClient.indices().exists(r -> r.index(INDEX_NAME)).value();

            if (exists) {
                // 기존 인덱스 삭제
                elasticsearchClient.indices().delete(d -> d.index(INDEX_NAME));
            }

            // 새로운 인덱스 생성
            String settings = """
                {
                    "settings": {
                        "analysis": {
                            "analyzer": {
                                "korean": {
                                    "type": "custom",
                                    "tokenizer": "nori_tokenizer",
                                    "filter": [
                                        "nori_readingform",
                                        "lowercase",
                                        "trim",
                                        "nori_part_of_speech"
                                    ]
                                }
                            }
                        },
                        "index": {
                            "max_ngram_diff": 50,
                            "highlight.max_analyzed_offset": 1000000
                        }
                    }
                }
                """;

            String mappings = """
                {
                    "mappings": {
                        "properties": {
                            "id": {"type": "keyword"},
                            "name": {
                                "type": "text",
                                "analyzer": "korean",
                                "search_analyzer": "korean",
                                "fields": {
                                    "keyword": {"type": "keyword"},
                                    "ngram": {
                                        "type": "text",
                                        "analyzer": "korean"
                                    }
                                }
                            },
                            "address1": {
                                "type": "text",
                                "analyzer": "korean",
                                "search_analyzer": "korean",
                                "fields": {
                                    "keyword": {"type": "keyword"},
                                    "ngram": {
                                        "type": "text",
                                        "analyzer": "korean"
                                    }
                                }
                            },
                            "latitude": {"type": "double"},
                            "longitude": {"type": "double"},
                            "location": {"type": "geo_point"}
                        }
                    }
                }
                """;

            elasticsearchClient.indices().create(c -> c
                    .index(INDEX_NAME)
                    .withJson(new StringReader(settings))
                    .withJson(new StringReader(mappings))
            );

            log.info("Successfully created index with Korean analyzer settings");
        } catch (Exception e) {
            log.error("Failed to initialize Elasticsearch index", e);
            throw new RuntimeException("Failed to initialize Elasticsearch index", e);
        }
    }

}
