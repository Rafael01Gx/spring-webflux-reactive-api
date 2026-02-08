package br.com.reactive.infra.client;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

@HttpExchange("/v2")
public interface DeeplClient {

    @PostExchange("/translate")
    Mono<TranslateResponse> translate(@RequestBody TranslateRequest request);
}
