package dev.com.protactic.dominio.principal;

import java.util.Date;

public class Fisico {
    private int id;
    private Jogador jogador;
    private String nome;
    private String musculo;
    private String intensidade;
    private String descricao;
    private Date dataInicio;
    private Date dataFim;
    private String status;

    public Fisico(int id, Jogador jogador, String nome, String musculo,
                  String intensidade, String descricao, Date dataInicio, Date dataFim, String status) {
        this.id = id;
        this.jogador = jogador;
        this.nome = nome;
        this.musculo = musculo;
        this.intensidade = intensidade;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Jogador getJogador() { return jogador; }
    public void setJogador(Jogador jogador) { this.jogador = jogador; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getMusculo() { return musculo; }
    public void setMusculo(String musculo) { this.musculo = musculo; }

    public String getIntensidade() { return intensidade; }
    public void setIntensidade(String intensidade) { this.intensidade = intensidade; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Date getDataInicio() { return dataInicio; }
    public void setDataInicio(Date dataInicio) { this.dataInicio = dataInicio; }

    public Date getDataFim() { return dataFim; }
    public void setDataFim(Date dataFim) { this.dataFim = dataFim; }

    public String getStatus() { return status; } 
    public void setStatus(String status) { this.status = status; }
}