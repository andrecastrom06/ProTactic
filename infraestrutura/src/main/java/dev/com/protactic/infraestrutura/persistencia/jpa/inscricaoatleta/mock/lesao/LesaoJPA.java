package dev.com.protactic.infraestrutura.persistencia.jpa.lesao;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity(name = "Lesao")
@Table(name = "Lesao") 
public class LesaoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_jogador")
    private Integer jogadorId;

    private boolean lesionado;
    private String tempo;
    private String plano;
    private int grau;

    public LesaoJPA() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Integer getJogadorId() { return jogadorId; }
    public void setJogadorId(Integer jogadorId) { this.jogadorId = jogadorId; }
    public boolean isLesionado() { return lesionado; }
    public void setLesionado(boolean lesionado) { this.lesionado = lesionado; }
    public String getTempo() { return tempo; }
    public void setTempo(String tempo) { this.tempo = tempo; }
    public String getPlano() { return plano; }
    public void setPlano(String plano) { this.plano = plano; }
    public int getGrau() { return grau; }
    public void setGrau(int grau) { this.grau = grau; }
}