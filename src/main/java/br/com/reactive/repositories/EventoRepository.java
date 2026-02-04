package br.com.reactive.repositories;

import br.com.reactive.domain.evento.Evento;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EventoRepository extends ReactiveCrudRepository<Evento,Long> {
}
