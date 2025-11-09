package dev.com.protactic.infraestrutura.persistencia.jpa.lesao;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity(name = "Lesao")
@Table(name = "Lesao") // 1. O nome da tabela DEVE ser igual ao do seu SQL
public class LesaoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // 2. Mapeia para a coluna 'id_jogador' (o ModelMapper vai preencher)
    @Column(name = "id_jogador")
    private Integer jogadorId;

    private boolean lesionado;
    private String tempo;
    private String plano;
    private int grau;

    // Construtor vazio obrigatório para o JPA
    public LesaoJPA() {}

    // --- Getters e Setters ---
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