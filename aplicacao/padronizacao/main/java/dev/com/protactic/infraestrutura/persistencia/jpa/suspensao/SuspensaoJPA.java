package dev.com.protactic.infraestrutura.persistencia.jpa.suspensao;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity(name = "Suspensao")
@Table(name = "suspensao")
public class SuspensaoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_jogador")
    private Integer idJogador;

    private boolean suspenso;
    private int amarelo;
    private int vermelho;

    public SuspensaoJPA() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getIdJogador() { return idJogador; }
    public void setIdJogador(Integer idJogador) { this.idJogador = idJogador; }
    public boolean isSuspenso() { return suspenso; }
    public void setSuspenso(boolean suspenso) { this.suspenso = suspenso; }
    public int getAmarelo() { return amarelo; }
    public void setAmarelo(int amarelo) { this.amarelo = amarelo; }
    public int getVermelho() { return vermelho; }
    public void setVermelho(int vermelho) { this.vermelho = vermelho; }
}