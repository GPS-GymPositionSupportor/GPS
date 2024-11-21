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

            if (!exists) {
                String mappings = """
                {
                    "mappings": {
                        "properties": {
                            "id": {"type": "keyword"},
                            "name": {"type": "text"},
                            "latitude": {"type": "double"},
                            "longitude": {"type": "double"},
                            "location": {"type": "geo_point"}
                        }
                    }
                }
                """;

                elasticsearchClient.indices().create(c -> c
                        .index(INDEX_NAME)
                        .withJson(new StringReader(mappings))
                );

                log.info("Created index with mappings: {}", mappings);
            }
        } catch (Exception e) {
            log.error("Failed to initialize Elasticsearch index", e);
            throw new RuntimeException("Failed to initialize Elasticsearch index", e);
        }
    }
}
