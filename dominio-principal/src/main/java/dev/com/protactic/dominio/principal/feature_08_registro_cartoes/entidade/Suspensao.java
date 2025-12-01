package dev.com.protactic.dominio.principal.feature_08_registro_cartoes.entidade;

public class Suspensao {
    private int id;
    private String atleta; 
    private boolean suspenso;
    private int amarelo;
    private int vermelho;

    public Suspensao() {}
    
    public Suspensao(int id, String atleta, boolean suspenso, int amarelo, int vermelho) {
        this.id = id;
        this.atleta = atleta;
        this.suspenso = suspenso;
        this.amarelo = amarelo;
        this.vermelho = vermelho;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAtleta() {
        return atleta;
    }

    public void setAtleta(String atleta) {
        this.atleta = atleta;
    }

    public boolean isSuspenso() {
        return suspenso;
    }

    public void setSuspenso(boolean suspenso) {
        this.suspenso = suspenso;
    }

    public int getAmarelo() {
        return amarelo;
    }

    public void setAmarelo(int amarelo) {
        this.amarelo = amarelo;
    }

    public int getVermelho() {
        return vermelho;
    }

    public void setVermelho(int vermelho) {
        this.vermelho = vermelho;
    }
}
