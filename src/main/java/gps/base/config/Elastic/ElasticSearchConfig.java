package gps.base.config.Elastic;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "gps.base.config.Elastic")
public class ElasticSearchConfig {

    @Bean
    public RestClient restClient() {

        return RestClient.builder(new HttpHost("localhost", 9200)).build();
    }

}