package br.com.reactive.application;

import br.com.reactive.infra.client.DeeplClient;
import br.com.reactive.infra.client.TranslateRequest;
import br.com.reactive.infra.client.TranslateResponse;
import reactor.core.publisher.Mono;

import java.util.Locale;

public class TranslationService {
    private final DeeplClient deeplClient;

    public TranslationService(DeeplClient deeplClient) {
        this.deeplClient = deeplClient;
    }

    public Mono<String> translate(Locale locale, String text) {
        return deeplClient.translate(new TranslateRequest(locale.getLanguage(), text))
                .map(TranslateResponse::translatedText);
    }
}
