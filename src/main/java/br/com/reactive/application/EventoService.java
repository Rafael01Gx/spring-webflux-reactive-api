package br.com.reactive.application;

import br.com.reactive.domain.evento.Evento;
import br.com.reactive.domain.evento.EventoDto;
import br.com.reactive.domain.evento.TipoEvento;
import br.com.reactive.infra.exeptions.NotFoundException;
import br.com.reactive.repositories.EventoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public Flux<EventoDto> findAll() {
        return eventoRepository.findAll()
                .map(EventoDto::toDto);
    }



    public Mono<EventoDto> findById(Long id) {
        return eventoRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Evento não encontrado!")))
                .map(EventoDto::toDto);
    }

    public Mono<EventoDto> create(EventoDto dto) {
        return eventoRepository.save(dto.toEntity()).map(EventoDto::toDto);
    }

    public Mono<EventoDto> update(Long id, EventoDto dto) {
        return eventoRepository.findById(id).switchIfEmpty(Mono.error(new NotFoundException("Evento náo encontrado!"))).flatMap(e->{
            e.setTipo(dto.tipo());
            e.setNome(dto.nome());
            e.setData(dto.data());
            e.setDescricao(dto.descricao());
            return eventoRepository.save(e);
        }).map(EventoDto::toDto);

    }

    public Mono<Void> delete(Long id) {
        return eventoRepository.deleteById(id);
    }

    public Flux<EventoDto> findByType(String tipo) {
        TipoEvento  tipoEvento = TipoEvento.valueOf(tipo.toUpperCase());

        return eventoRepository.findByTipo(tipoEvento).map(EventoDto::toDto);
    }

    public Mono<Integer> qnTicketsAvailable(Long id) {
        return eventoRepository.findById(id).switchIfEmpty(Mono.error(new NotFoundException("Evento não encontrado"))).map(
                Evento::getQntIngressos
        );
    }
}
