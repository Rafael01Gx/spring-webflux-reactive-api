package br.com.reactive.infra.client;

import java.util.List;

public record TranslateResponse(
       List<DeepResponse> translations
) {

    public String translatedText(){
        return this.translations.getFirst().text();
    }
}
