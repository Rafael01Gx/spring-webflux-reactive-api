package br.com.reactive.domain.evento;

import java.time.LocalDate;

public record EventoDto(
        Long id,
        TipoEvento tipo,
        String nome,
        LocalDate data,
        Integer qntIngressos,
        String descricao
) {

    public static EventoDto toDto(Evento evento){
      return new EventoDto(evento.getId(),evento.getTipo(),evento.getNome(),evento.getData(), evento.getQntIngressos(), evento.getDescricao());
    }

    public Evento toEntity(){
        return  new Evento.Builder()
                .id(this.id())
                .tipo(this.tipo())
                .nome(this.nome())
                .data(this.data())
                .qntIngressos(this.qntIngressos())
                .descricao(this.descricao())
                .build();
    }
}
