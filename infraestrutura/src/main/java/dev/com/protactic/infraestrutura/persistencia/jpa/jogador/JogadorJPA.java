package dev.com.protactic.infraestrutura.persistencia.jpa.jogador;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column; // Importante para mapear nomes diferentes
import java.time.LocalDate;

@Entity(name = "Jogador")
@Table(name = "Jogador") // 1. O nome da tabela DEVE ser igual ao do seu SQL
public class JogadorJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    // 2. Mapeia os nomes do Java (camelCase) para os nomes do SQL (snake_case)
    @Column(name = "id_contrato")
    private Integer contratoId;
    
    @Column(name = "id_clube")
    private Integer clubeId;
    
    @Column(name = "id_competicao")
    private Integer competicaoId;

    private String nome;
    private int idade;
    private String posicao;
    private String perna;
    private double nota;
    private int jogos;
    private int gols;
    private int assistencias;
    
    // --- Colunas que adicionámos ---
    private String status;
    private String minutagem;
    
    @Column(name = "chegada_no_clube")
    private LocalDate chegadaNoClube;
    
    private boolean capitao;
    
    @Column(name = "grau_lesao")
    private int grauLesao;
    
    @Column(name = "contrato_ativo")
    private boolean contratoAtivo;
    
    private boolean saudavel;
    
    @Column(name = "desvio_padrao")
    private double desvioPadrao;

    // Construtor vazio obrigatório para o JPA
    public JogadorJPA() {}

    // --- Getters e Setters ---
    // (O ModelMapper e o JPA precisam de todos eles)

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Integer getContratoId() { return contratoId; }
    public void setContratoId(Integer contratoId) { this.contratoId = contratoId; }
    public Integer getClubeId() { return clubeId; }
    public void setClubeId(Integer clubeId) { this.clubeId = clubeId; }
    public Integer getCompeticaoId() { return competicaoId; }
    public void setCompeticaoId(Integer competicaoId) { this.competicaoId = competicaoId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }
    public String getPosicao() { return posicao; }
    public void setPosicao(String posicao) { this.posicao = posicao; }
    public String getPerna() { return perna; }
    public void setPerna(String perna) { this.perna = perna; }
    public double getNota() { return nota; }
    public void setNota(double nota) { this.nota = nota; }
    public int getJogos() { return jogos; }
    public void setJogos(int jogos) { this.jogos = jogos; }
    public int getGols() { return gols; }
    public void setGols(int gols) { this.gols = gols; }
    public int getAssistencias() { return assistencias; }
    public void setAssistencias(int assistencias) { this.assistencias = assistencias; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMinutagem() { return minutagem; }
    public void setMinutagem(String minutagem) { this.minutagem = minutagem; }
    public LocalDate getChegadaNoClube() { return chegadaNoClube; }
    public void setChegadaNoClube(LocalDate chegadaNoClube) { this.chegadaNoClube = chegadaNoClube; }
    public boolean isCapitao() { return capitao; }
    public void setCapitao(boolean capitao) { this.capitao = capitao; }
    public int getGrauLesao() { return grauLesao; }
    public void setGrauLesao(int grauLesao) { this.grauLesao = grauLesao; }
    public boolean isContratoAtivo() { return contratoAtivo; }
    public void setContratoAtivo(boolean contratoAtivo) { this.contratoAtivo = contratoAtivo; }
    public boolean isSaudavel() { return saudavel; }
    public void setSaudavel(boolean saudavel) { this.saudavel = saudavel; }
    public double getDesvioPadrao() { return desvioPadrao; }
    public void setDesvioPadrao(double desvioPadrao) { this.desvioPadrao = desvioPadrao; }
}