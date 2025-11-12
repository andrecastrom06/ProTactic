package dev.com.protactic.infraestrutura.persistencia.jpa.inscricaoatleta;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.IdClass; 

@Entity(name = "InscricaoAtleta")
@Table(name = "inscricao_atleta")
@IdClass(InscricaoAtletaPK.class)
public class InscricaoAtletaJPA {

    @Id
    private String atleta;
    
    @Id
    private String competicao;

    @Column(name = "elegivel_para_jogos")
    private boolean elegivelParaJogos;
    
    private boolean inscrito;
    
    @Column(name = "mensagem_erro")
    private String mensagemErro;

    public InscricaoAtletaJPA() {}

    public String getAtleta() { return atleta; }
    public void setAtleta(String atleta) { this.atleta = atleta; }
    public String getCompeticao() { return competicao; }
    public void setCompeticao(String competicao) { this.competicao = competicao; }
    public boolean isElegivelParaJogos() { return elegivelParaJogos; }
    public void setElegivelParaJogos(boolean elegivelParaJogos) { this.elegivelParaJogos = elegivelParaJogos; }
    public boolean isInscrito() { return inscrito; }
    public void setInscrito(boolean inscrito) { this.inscrito = inscrito; }
    public String getMensagemErro() { return mensagemErro; }
    public void setMensagemErro(String mensagemErro) { this.mensagemErro = mensagemErro; }
}