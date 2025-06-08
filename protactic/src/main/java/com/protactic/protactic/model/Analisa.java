package com.protactic.protactic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Analisa")
@IdClass(AnalisaId.class)
public class Analisa {

    @Id
    @ManyToOne
    @JoinColumn(name = "fk_Analista_CPF")
    private AnalistaDeDesempenho analista;

    @Id
    @ManyToOne
    @JoinColumn(name = "fk_Jogador_CPF")
    private Jogador jogador;

    public AnalistaDeDesempenho getAnalista() {
        return analista;
    }

    public void setAnalista(AnalistaDeDesempenho analista) {
        this.analista = analista;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }
}