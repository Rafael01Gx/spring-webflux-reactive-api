package br.com.reactive.domain.evento;

import java.time.LocalDate;

public record EventoDto(
        Long id,
        TipoEvento tipo,
        String nome,
        LocalDate data,
        String descricao
) {

    public static EventoDto toDto(Evento evento){
      return new EventoDto(evento.getId(),evento.getTipo(),evento.getNome(),evento.getData(),evento.getDescricao());
    }

    public Evento toEntity(){
        return  new Evento.Builder()
                .id(this.id())
                .tipo(this.tipo())
                .nome(this.nome())
                .data(this.data())
                .descricao(this.descricao())
                .build();
    }
}
