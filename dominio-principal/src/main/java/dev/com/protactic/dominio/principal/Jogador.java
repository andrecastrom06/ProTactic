package dev.com.protactic.dominio.principal;

import java.time.LocalDate;
import java.util.Objects;

public class Jogador {

    private int id;
    
    // MUDANÇA: Referências a outros Agregados (Contrato, Clube, Competicao)
    // foram trocadas por seus IDs.
    // private Contrato contrato;
    // private Clube clube;
    // private Competicao competicao;
    private Integer contratoId;
    private Integer clubeId;
    private Integer competicaoId;

    private String nome;
    private int idade;
    private String posicao;
    private String perna;
    private double nota;
    private int jogos;
    private int gols;
    private int assistencias;
    private String status;
    private String minutagem;   
    private LocalDate chegadaNoClube;     
    private boolean capitao;    
    private int grauLesao = -1;         
    private boolean contratoAtivo = false;
    private boolean saudavel;

    // MUDANÇA: Construtor atualizado para receber IDs.
    public Jogador(int id, Integer contratoId, Integer clubeId, Integer competicaoId, String nome, int idade,
                   String posicao, String perna, double nota, int jogos, int gols, int assistencias,
                   String minutagem, LocalDate chegadaNoClube, boolean capitao) {
        this.id = id;
        this.contratoId = contratoId;
        this.clubeId = clubeId;
        this.competicaoId = competicaoId;
        this.nome = nome;
        this.idade = idade;
        this.posicao = posicao;
        this.perna = perna;
        this.nota = nota;
        this.jogos = jogos;
        this.gols = gols;
        this.assistencias = assistencias;
        this.minutagem = minutagem;
        this.chegadaNoClube = chegadaNoClube;
        this.capitao = capitao;
    }

    // MUDANÇA: Construtor atualizado para receber IDs.
    public Jogador(int id, Integer contratoId, Integer clubeId, Integer competicaoId, String nome, int idade,
                   String posicao, String perna, double nota, int jogos, int gols, int assistencias) {
        this(id, contratoId, clubeId, competicaoId, nome, idade, posicao, perna, nota, jogos, gols, assistencias,
             null, null, false);
    }

    // MUDANÇA: Construtor atualizado para receber IDs.
    public Jogador(String nome, Integer clubeId) {
        this.nome = nome;
        this.clubeId = clubeId;
        
        // REMOVIDO: Lógica perigosa que criava outro Agregado (Contrato)
        // if (clube != null) {
        //     this.contrato = new Contrato(clube);
        // }
        
        this.minutagem = null;
        this.chegadaNoClube = null;
        this.capitao = false;
    }

    // MUDANÇA: Construtor atualizado para receber ID.
    public Jogador(Integer contratoId) {
        this.contratoId = contratoId;
        this.minutagem = null;
        this.chegadaNoClube = null;
        this.capitao = false;
    }

    public Jogador(String nome) {
        this.nome = nome;
        this.minutagem = null;
        this.chegadaNoClube = null;
        this.capitao = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    // MUDANÇA: Getters e Setters para os IDs.
    public Integer getContratoId() { return contratoId; }
    public void setContratoId(Integer contratoId) {
        this.contratoId = contratoId;
        
        // REMOVIDO: Lógica perigosa que alterava o estado (clube)
        // baseado em outro agregado (contrato).
        // if (contrato != null) {
        //     this.clube = contrato.getClube();
        // }
    }

    // MUDANÇA: Getters e Setters para os IDs.
    public Integer getClubeId() { return clubeId; }
    public void setClubeId(Integer clubeId) { this.clubeId = clubeId; }

    // MUDANÇA: Getters e Setters para os IDs.
    public Integer getCompeticaoId() { return competicaoId; }
    public void setCompeticaoId(Integer competicaoId) { this.competicaoId = competicaoId; }

    // --- Métodos restantes permanecem os mesmos ---
    
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

    public boolean isDisponivel() {
        return "disponível".equalsIgnoreCase(this.status);
    }

    public String getMinutagem() { return minutagem; }
    public void setMinutagem(String minutagem) { this.minutagem = minutagem; }

    public LocalDate getChegadaNoClube() { return chegadaNoClube; }
    public void setChegadaNoClube(LocalDate chegadaNoClube) { this.chegadaNoClube = chegadaNoClube; }

    public boolean isCapitao() { return capitao; }
    public void setCapitao(boolean capitao) { this.capitao = capitao; }

    public int getGrauLesao() {
        return grauLesao;
    }

    public void setGrauLesao(int grauLesao) {
        this.grauLesao = grauLesao;
    }

    public boolean isContratoAtivo() {
        return contratoAtivo;
    }

    public void setContratoAtivo(boolean contratoAtivo) {
        this.contratoAtivo = contratoAtivo;
    }

    public boolean isSaudavel() {
        return saudavel;
    }

    public void setSaudavel(boolean saudavel) {
        this.saudavel = saudavel;
    }

    private double desvioPadrao = 0.0;

    public double getDesvioPadrao() {
        return desvioPadrao;
    }

    public void setDesvioPadrao(double desvioPadrao) {
        this.desvioPadrao = desvioPadrao;
    }

    // MUDANÇA: equals/hashCode agora são baseados no ID (correto para Entidades)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jogador jogador = (Jogador) o;
        // Se os IDs não foram setados (novos objetos), não podem ser iguais
        if (this.id == 0 || jogador.id == 0) return false;
        return id == jogador.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}