package br.com.reactive.infra.config;

import br.com.reactive.infra.client.DeeplClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@EnableConfigurationProperties(DeeplProperties.class)
public class RestClientConfig {


    @Bean
    WebClient deeplWebClient(DeeplProperties properties) {
        return WebClient.builder()
                .baseUrl(properties.apiBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    DeeplClient deeplClient(WebClient deeplWebClient) {
        var adapter = WebClientAdapter.create(deeplWebClient);

        return HttpServiceProxyFactory
                .builderFor(adapter)
                .build()
                .createClient(DeeplClient.class);
    }

}
