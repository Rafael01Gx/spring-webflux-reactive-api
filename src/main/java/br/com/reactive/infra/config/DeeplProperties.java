package br.com.reactive.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "deepl")
public record DeeplProperties(
        String apiBaseUrl,
        String apiKey
) {
}
