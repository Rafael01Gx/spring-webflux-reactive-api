package br.com.reactive.repositories;

import br.com.reactive.domain.evento.Evento;
import br.com.reactive.domain.evento.TipoEvento;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface EventoRepository extends ReactiveCrudRepository<Evento,Long> {
    Flux<Evento> findByTipo(TipoEvento tipoEvento);
}
