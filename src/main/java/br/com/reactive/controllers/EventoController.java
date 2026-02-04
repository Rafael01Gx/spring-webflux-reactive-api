package br.com.reactive.controllers;

import br.com.reactive.application.EventoService;
import br.com.reactive.domain.evento.EventoDto;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService service;

    EventoController(EventoService service) {
        this.service = service;
    }


    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<EventoDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<EventoDto> findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Mono<EventoDto> save(@RequestBody @Validated EventoDto dto) {
        return service.create(dto);
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
