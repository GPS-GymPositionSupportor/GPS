package gps.base.config.Elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ElasticsearchIndexInitializer {
    private final ElasticsearchClient elasticsearchClient;

    public ElasticsearchIndexInitializer(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @PostConstruct
    public void init() {
        try {
            createGymIndex();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create elasticsearch index", e);
        }
    }

    private void createGymIndex() throws IOException {
        boolean indexExists = elasticsearchClient.indices()
                .exists(e -> e.index("gyms"))
                .value();

        if (!indexExists) {
            // 인덱스 생성 설정
            CreateIndexRequest request = CreateIndexRequest.of(b -> b
                    .index("gyms")
                    .mappings(mappings -> mappings
                            .properties("id", property -> property.keyword(k -> k))
                            .properties("name", property -> property.text(t -> t))
                            .properties("category", property -> property.keyword(k -> k))
                            .properties("address", property -> property.text(t -> t))
                            .properties("location", property -> property.geoPoint(g -> g))
                            .properties("rating", property -> property.double_(d -> d))
                            .properties("openingHours", property -> property.text(t -> t))
                            .properties("phoneNumber", property -> property.keyword(k -> k))
                            .properties("userInteractions", property -> property
                                    .nested(n -> n
                                            .properties("userId", p -> p.keyword(k -> k))
                                            .properties("type", p -> p.keyword(k -> k))
                                            .properties("rating", p -> p.double_(d -> d))
                                            .properties("timestamp", p -> p.date(d -> d))
                                    )
                            )
                    )
            );

            elasticsearchClient.indices().create(request);
        }
    }
}
