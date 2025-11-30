package dev.com.protactic.dominio.principal;

import java.util.Date;

public class Premiacao {
    private int id;
    private Jogador jogador;
    private String nome;
    private Date dataPremiacao;

    public Premiacao(int id, Jogador jogador, String nome, Date dataPremiacao) {
        this.id = id;
        this.jogador = jogador;
        this.nome = nome;
        this.dataPremiacao = dataPremiacao;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Jogador getJogador() { return jogador; }
    public void setJogador(Jogador jogador) { this.jogador = jogador; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Date getDataPremiacao() { return dataPremiacao; }
    public void setDataPremiacao(Date dataPremiacao) { this.dataPremiacao = dataPremiacao; }
}