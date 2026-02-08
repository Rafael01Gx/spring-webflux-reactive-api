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
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient deeplWebClient(WebClient.Builder builder,DeeplProperties properties) {
        return builder
                .baseUrl(properties.apiBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION,"DeepL-Auth-Key " + properties.apiKey())
                .build();
    }

    @Bean
    public WebClientAdapter deeplWebClientAdapter(WebClient deeplWebClient) {
        return WebClientAdapter.create(deeplWebClient);
    }

    @Bean
    public DeeplClient deeplClient(WebClientAdapter deeplWebClientAdapter) {
        return HttpServiceProxyFactory
                .builderFor(deeplWebClientAdapter)
                .build()
                .createClient(DeeplClient.class);
    }

}
