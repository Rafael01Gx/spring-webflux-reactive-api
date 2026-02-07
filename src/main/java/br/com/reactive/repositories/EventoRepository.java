package br.com.reactive.repositories;

import br.com.reactive.domain.evento.Evento;
import br.com.reactive.domain.evento.TipoEvento;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventoRepository extends ReactiveCrudRepository<Evento, Long> {
    Flux<Evento> findByTipo(TipoEvento tipoEvento);

//    @Modifying
    @Query("""
            UPDATE eventos
            SET qnt_ingressos = qnt_ingressos - 1
            WHERE id = :id AND qnt_ingressos > 0
            RETURNING qnt_ingressos
            """)
    Mono<Integer> reservarIngresso(Long id);
}