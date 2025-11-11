package dev.com.protactic.infraestrutura.persistencia.jpa.escalacao;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity(name = "EscalacaoSimples")
@Table(name = "escalacao_simples")
public class EscalacaoSimplesJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "jogo_data")
    private String jogoData;

    @Column(name = "nome_jogador")
    private String nomeJogador;

    public EscalacaoSimplesJPA() {}

    public EscalacaoSimplesJPA(String jogoData, String nomeJogador) {
        this.jogoData = jogoData;
        this.nomeJogador = nomeJogador;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getJogoData() { return jogoData; }
    public void setJogoData(String jogoData) { this.jogoData = jogoData; }
    public String getNomeJogador() { return nomeJogador; }
    public void setNomeJogador(String nomeJogador) { this.nomeJogador = nomeJogador; }
}