package br.com.reactive.controllers;

import br.com.reactive.application.EventoService;
import br.com.reactive.domain.evento.EventoDto;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService service;
    private final Sinks.Many<EventoDto> eventoSink;

    EventoController(EventoService service) {
        this.service = service;
        this.eventoSink = Sinks.many().multicast().onBackpressureBuffer();
    }


    @GetMapping
    public Flux<EventoDto> findAll() {
        return service.findAll();
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "categoria/{tipo}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<EventoDto> findByType(@PathVariable("tipo") String tipo) {
        return Flux.merge(service.findByType(tipo)
                ,eventoSink.asFlux())
                .delayElements(Duration.ofSeconds(3));
    }

    @GetMapping("/{id}")
    public Mono<EventoDto> findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Mono<EventoDto> save(@RequestBody @Validated EventoDto dto) {

        return service.create(dto)
                .doOnSuccess(e-> eventoSink.tryEmitNext(dto));
    }

    @PutMapping("{id}")
    public Mono<EventoDto> update(@PathVariable Long id, @RequestBody EventoDto dto) {
        return service.update(id,dto);
    }

    @DeleteMapping("{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return service.delete(id);
    }

}
