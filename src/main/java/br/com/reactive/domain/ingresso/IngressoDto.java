package br.com.reactive.domain.ingresso;

import br.com.reactive.domain.evento.Evento;


public record IngressoDto(
        Long id,
        String codigo,
        Long eventoId
) {

    public IngressoDto(Ingresso ingresso){
        this(ingresso.getId(),ingresso.getCodigo(),ingresso.getId());
    }

    public Ingresso toEntity(){
        return new Ingresso(this.eventoId());
    }
}
