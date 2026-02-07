package br.com.reactive.domain.ingresso;

import br.com.reactive.domain.evento.Evento;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Table("ingressos")
public class Ingresso {

    @Id
    private Long id;
    private String codigo;
    @Column("evento_id")
    private Long eventoId;


    public Ingresso(Long eventoId) {
        this.codigo = UUID.randomUUID().toString() + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        this.eventoId = eventoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }
}
