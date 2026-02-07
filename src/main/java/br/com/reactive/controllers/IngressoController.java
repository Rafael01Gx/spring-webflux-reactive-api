package br.com.reactive.controllers;

import br.com.reactive.application.EventoSinkService;
import br.com.reactive.application.IngressoService;
import br.com.reactive.domain.ingresso.Ingresso;
import br.com.reactive.domain.ingresso.IngressoDto;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ingressos")
public class IngressoController {
    private final IngressoService ingressoService;


    public IngressoController(IngressoService ingressoService, EventoSinkService eventoSinkService) {
        this.ingressoService = ingressoService;
    }

    @GetMapping("{id}")
    public Mono<Ingresso> getIngresso(@PathVariable Long id) {
        return ingressoService.getIngresso(id);
    }

    @PostMapping("comprar")
    public Mono<IngressoDto> buyIngresso(@RequestBody IngressoDto dto) {
        return ingressoService.buyIngresso(dto);
    }

}
