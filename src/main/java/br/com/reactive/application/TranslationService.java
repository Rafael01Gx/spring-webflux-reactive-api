package br.com.reactive.application;

import br.com.reactive.infra.client.DeeplClient;
import br.com.reactive.infra.client.TranslateRequest;
import br.com.reactive.infra.client.TranslateResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TranslationService {
    private final DeeplClient deeplClient;

    public TranslationService(DeeplClient deeplClient) {
        this.deeplClient = deeplClient;

    }

    public Mono<String> translate(String locale, String text) {
        return deeplClient.translate(new TranslateRequest(locale,text))
                .map(TranslateResponse::translatedText);
    }


}
