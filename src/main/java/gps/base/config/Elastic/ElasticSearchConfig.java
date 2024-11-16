package gps.base.config.Elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;

import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        // ElasticsearchClient 생성 코드
        RestClient restClient = RestClient.builder(
                HttpHost.create("http://localhost:9200")
        ).build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper()
        );

        return new ElasticsearchClient(transport);
    }

    @Bean
    public ElasticsearchIndexInitializer elasticsearchIndexInitializer(ElasticsearchClient client) {
        return new ElasticsearchIndexInitializer(client);
    }
}

