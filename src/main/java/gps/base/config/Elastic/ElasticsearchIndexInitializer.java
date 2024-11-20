package gps.base.config.Elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ElasticsearchIndexInitializer {
    private final ElasticsearchClient elasticsearchClient;
    private static final String INDEX_NAME = "gyms";

    public ElasticsearchIndexInitializer(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @PostConstruct
    public void init() {
        try {
            boolean indexExists = elasticsearchClient.indices().exists(e -> e.index(INDEX_NAME)).value();

            if (!indexExists) {
                elasticsearchClient.indices().create(c -> c
                        .index(INDEX_NAME)
                        .mappings(m -> m
                                .properties("id", p -> p.keyword(k -> k))
                                .properties("name", p -> p.text(t -> t))
                                .properties("category", p -> p.keyword(k -> k))
                                .properties("address", p -> p.text(t -> t))
                                .properties("location", p -> p.geoPoint(g -> g))
                                .properties("rating", p -> p.double_(d -> d))
                                .properties("openingHours", p -> p.text(t -> t))
                                .properties("phoneNumber", p -> p.keyword(k -> k))
                                .properties("userInteractions", p -> p.nested(n -> n
                                        .properties("userId", np -> np.keyword(k -> k))
                                        .properties("rating", np -> np.double_(d -> d))
                                ))
                        )
                );
                log.info("Index created successfully");
            }
        } catch (Exception e) {
            log.error("Index initialization error: ", e);
            throw new RuntimeException("Failed to initialize elasticsearch index", e);
        }
    }
}