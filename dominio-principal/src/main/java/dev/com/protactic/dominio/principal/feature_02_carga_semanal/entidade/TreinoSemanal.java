package dev.com.protactic.dominio.principal.feature_02_carga_semanal.entidade;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TreinoSemanal {

    private Long id;
    private Jogador jogador;
    private LocalDateTime dataTreino;
    private boolean registrado;

    // --- NOVOS CAMPOS PARA O STRATEGY ---
    private double duracaoMinutos;
    private double intensidade; // 0 a 10
    private BigDecimal cargaTotal; // O valor calculado pela estratégia
    // ------------------------------------

    public TreinoSemanal() {
        this.dataTreino = LocalDateTime.now();
    }

    public TreinoSemanal(Jogador jogador) {
        this();
        this.jogador = jogador;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Jogador getJogador() { return jogador; }
    public void setJogador(Jogador jogador) { this.jogador = jogador; }

    public LocalDateTime getDataTreino() { return dataTreino; }
    public void setDataTreino(LocalDateTime dataTreino) { this.dataTreino = dataTreino; }

    public boolean isRegistrado() { return registrado; }
    public void setRegistrado(boolean registrado) { this.registrado = registrado; }

    // --- NOVOS GETTERS E SETTERS ---
    public double getDuracaoMinutos() { return duracaoMinutos; }
    public void setDuracaoMinutos(double duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }

    public double getIntensidade() { return intensidade; }
    public void setIntensidade(double intensidade) { this.intensidade = intensidade; }

    public BigDecimal getCargaTotal() { return cargaTotal; }
    public void setCargaTotal(BigDecimal cargaTotal) { this.cargaTotal = cargaTotal; }
}