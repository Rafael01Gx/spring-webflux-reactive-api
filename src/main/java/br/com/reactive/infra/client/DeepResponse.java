package br.com.reactive.infra.client;

public record DeepResponse(
        String detected_source_language,
        String text
){
}
