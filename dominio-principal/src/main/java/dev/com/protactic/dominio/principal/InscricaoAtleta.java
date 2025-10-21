package dev.com.protactic.dominio.principal;

public class InscricaoAtleta {

    private final String atleta;
    private final String competicao;
    private boolean elegivelParaJogos;
    private boolean inscrito;
    private String mensagemErro;

    public InscricaoAtleta(String atleta, String competicao) {
        this.atleta = atleta;
        this.competicao = competicao;
        this.elegivelParaJogos = false;
        this.inscrito = false;
        this.mensagemErro = null;
    }

    public String getAtleta() {
        return atleta;
    }

    public String getCompeticao() {
        return competicao;
    }

    public boolean isElegivelParaJogos() {
        return elegivelParaJogos;
    }

    public boolean isInscrito() {
        return inscrito;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

    public void setElegivelParaJogos(boolean elegivelParaJogos) {
        this.elegivelParaJogos = elegivelParaJogos;
    }

    public void setInscrito(boolean inscrito) {
        this.inscrito = inscrito;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }
}
