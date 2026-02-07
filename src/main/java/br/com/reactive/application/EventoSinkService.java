package br.com.reactive.application;

import br.com.reactive.domain.evento.EventoDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class EventoSinkService {

    private final Sinks.Many<EventoDto> eventoSink = Sinks.many().multicast().onBackpressureBuffer();
    private final Sinks.Many<Integer> qntIngressoSink = Sinks.many().multicast().onBackpressureBuffer();


    public Flux<EventoDto> getFluxEvento() {
        return eventoSink.asFlux();
    }


    public void emitEvento(EventoDto evento) {
        eventoSink.tryEmitNext(evento)
                .orThrow();
    }

    public Flux<Integer> getQntIngresso() {
        return qntIngressoSink.asFlux();
    }

    public void emitQntIngresso(Integer qntIngresso) {
        qntIngressoSink.tryEmitNext(qntIngresso)
                .orThrow();
    }

}
