package br.com.reactive.infra.client;

public record TranslateRequest(
        String target_lang,
        String[] text
) {

    public TranslateRequest(String locale, String text) {
        String normalized = locale.replace('_', '-').toLowerCase();

        String lang = switch (normalized) {
            case "pt-br" -> "PT-BR";
            case "pt-pt", "pt" -> "PT-PT";
            case "en-gb" -> "EN-GB";
            case "en", "en-us" -> "EN";
            default -> normalized.toUpperCase();
        };

        this(lang, new String[]{ text });
    }



}
