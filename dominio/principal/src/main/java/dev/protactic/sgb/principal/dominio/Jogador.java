package dev.protactic.sgb.principal.dominio;

public class Jogador {
    private int id;
    private Contrato contrato;
    private Clube clube;
    private Competicao competicao;
    private String nome;
    private int idade;
    private String posicao;
    private String perna;
    private double nota;
    private int jogos;
    private int gols;
    private int assistencias;

    public Jogador(int id, Contrato contrato, Clube clube, Competicao competicao, String nome, int idade,
                   String posicao, String perna, double nota, int jogos, int gols, int assistencias) {
        this.id = id;
        this.contrato = contrato;
        this.clube = clube;
        this.competicao = competicao;
        this.nome = nome;
        this.idade = idade;
        this.posicao = posicao;
        this.perna = perna;
        this.nota = nota;
        this.jogos = jogos;
        this.gols = gols;
        this.assistencias = assistencias;
    }

    public Jogador(String nome, Clube clube){
        this.clube = clube;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public Clube getClube() {
        return clube;
    }

    public void setClube(Clube clube) {
        this.clube = clube;
    }

    public Competicao getCompeticao() {
        return competicao;
    }

    public void setCompeticao(Competicao competicao) {
        this.competicao = competicao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getPosicao() {
        return posicao;
    }

    public void setPosicao(String posicao) {
        this.posicao = posicao;
    }

    public String getPerna() {
        return perna;
    }

    public void setPerna(String perna) {
        this.perna = perna;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public int getJogos() {
        return jogos;
    }

    public void setJogos(int jogos) {
        this.jogos = jogos;
    }

    public int getGols() {
        return gols;
    }

    public void setGols(int gols) {
        this.gols = gols;
    }

    public int getAssistencias() {
        return assistencias;
    }

    public void setAssistencias(int assistencias) {
        this.assistencias = assistencias;
    }
}