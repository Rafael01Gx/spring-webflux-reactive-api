package br.com.reactive.controllers;

import br.com.reactive.application.EventoService;
import br.com.reactive.application.EventoSinkService;
import br.com.reactive.domain.evento.EventoDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService service;
    private final EventoSinkService eventoSinkService;



    EventoController(EventoService service, EventoSinkService eventoSinkService) {
        this.service = service;
        this.eventoSinkService =  eventoSinkService;

    }


    @GetMapping
    public Flux<EventoDto> findAll() {
        return service.findAll();
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "categoria/{tipo}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<EventoDto> findByType(@PathVariable("tipo") String tipo) {
        return Flux.merge(service.findByType(tipo)
                ,eventoSinkService.getFluxEvento())
                .delayElements(Duration.ofSeconds(3));
    }
    @GetMapping(value = "ingresso/{id}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> qnTicketsAvailable(@PathVariable Long id){
        return Flux.merge(service.qnTicketsAvailable(id)
                ,eventoSinkService.getQntIngresso()).delayElements(Duration.ofSeconds(3));
    }

    @GetMapping(value = "/{id}")
    public Mono<EventoDto> findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EventoDto> save(@RequestBody @Validated EventoDto dto) {

        return service.create(dto)
                .doOnSuccess(e-> eventoSinkService.emitEvento(dto));
    }

    @PutMapping("{id}")
    public Mono<EventoDto> update(@PathVariable Long id, @RequestBody EventoDto dto) {
        return service.update(id,dto);
    }

    @DeleteMapping("{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @PostMapping("{id}/traduzir")
    public Mono<EventoDto> translate(@PathVariable Long id, @RequestHeader HttpHeaders header) {
        return service.translate(id,header.getAcceptLanguage().getFirst().toString());
    }


}
