package br.com.reactive.domain.evento;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table(name = "eventos")
public class Evento {

    @Id
    private Long id;
    private TipoEvento tipo;
    private String nome;
    private LocalDate data;
    private String descricao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public void setTipo(TipoEvento tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    public static class Builder{
        private Evento evento;

        public Builder(){
            evento = new Evento();
        }

        public Builder id(Long id){
            evento.setId(id);
            return this;
        }

        public Builder nome(String nome){
            evento.setNome(nome);
            return this;
        }

        public Builder data(LocalDate data){
            evento.setData(data);
            return this;
        }

        public Builder tipo(TipoEvento tipo){
            evento.setTipo(tipo);
            return this;
        }
        public Builder descricao(String descricao){
            evento.setDescricao(descricao);
            return this;
        }
        public Evento build(){
            return evento;

        }
    }
}
