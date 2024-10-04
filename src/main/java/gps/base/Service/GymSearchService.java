package gps.base.Service;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import gps.base.config.Elastic.GymDocument;
import gps.base.config.Elastic.GymSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GymSearchService {

    private final GymSearchRepository gymSearchRepository;
    private final ElasticsearchClient elasticsearchClient;

    public List<GymDocument> searchGyms(String keyword) throws IOException {
        Query query = MatchQuery.of(m -> m
                .field("name")
                .field("address")
                .field("description")
                .field("category")
                .query(keyword)
        )._toQuery();

        SearchResponse<GymDocument> searchResponse = elasticsearchClient.search(s -> s
                        .index("gym")
                        .query(query),
                GymDocument.class
        );

        return searchResponse.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    public GymDocument save(GymDocument gymDocument) {
        return gymSearchRepository.save(gymDocument);
    }
}