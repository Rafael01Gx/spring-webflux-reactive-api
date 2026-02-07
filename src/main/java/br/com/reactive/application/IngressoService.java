package br.com.reactive.application;

import br.com.reactive.domain.ingresso.Ingresso;
import br.com.reactive.domain.ingresso.IngressoDto;
import br.com.reactive.infra.exeptions.NotFoundException;
import br.com.reactive.infra.exeptions.TicketUnavailable;
import br.com.reactive.repositories.EventoRepository;
import br.com.reactive.repositories.IngressoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

@Service
public class IngressoService {

    private final IngressoRepository ingressoRepository;
    private final EventoRepository eventoRepository;
    private final EventoSinkService eventoSinkService;

    public IngressoService(IngressoRepository ingressoRepository, EventoRepository eventoRepository, EventoSinkService eventoSinkService) {
        this.ingressoRepository = ingressoRepository;
        this.eventoRepository = eventoRepository;
        this.eventoSinkService = eventoSinkService;
    }

    public Mono<Ingresso> getIngresso(Long id) {
        return ingressoRepository.findById(id).switchIfEmpty(Mono.error(new NotFoundException("Ingresso not found")));
    }

    public Mono<IngressoDto> buyIngresso(IngressoDto dto) {

        return eventoRepository.reservarIngresso(dto.eventoId())
                .switchIfEmpty(Mono.error(new TicketUnavailable("Ingressos esgotados")))
                .flatMap(qntAntes ->
                        ingressoRepository.save(dto.toEntity())
                                .map(IngressoDto::new)
                                .doOnSuccess(x -> eventoSinkService.emitQntIngresso(qntAntes))
                );


//        return transactionalOperator.execute(status ->
//                eventoRepository.reservarIngresso(showId)
//                        .filter(rows -> rows > 0)
//                        .switchIfEmpty(Mono.error(new TicketUnavailable("Ingressos esgotados")))
//                        .then(ingressoRepository.save(dto.toEntity()))
//                        .map(IngressoDto::new)
//        );

    }

}
