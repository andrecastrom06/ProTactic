package dev.com.protactic.dominio.principal.feature_11_premiacao_interna.entidade;

import java.math.BigDecimal;
import java.util.Date;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;

public class Premiacao {
    private int id;
    private Jogador jogador;
    private String nome;
    private Date dataPremiacao;
    private BigDecimal valor; 

    public Premiacao() {}

    public Premiacao(int id, Jogador jogador, String nome, Date dataPremiacao, BigDecimal valor) {
        this.id = id;
        this.jogador = jogador;
        this.nome = nome;
        this.dataPremiacao = dataPremiacao;
        this.valor = valor;
    }

    public Premiacao(int id, Jogador jogador, String nome, Date dataPremiacao) {
        this(id, jogador, nome, dataPremiacao, BigDecimal.ZERO);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Jogador getJogador() { return jogador; }
    public void setJogador(Jogador jogador) { this.jogador = jogador; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Date getDataPremiacao() { return dataPremiacao; }
    public void setDataPremiacao(Date dataPremiacao) { this.dataPremiacao = dataPremiacao; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}