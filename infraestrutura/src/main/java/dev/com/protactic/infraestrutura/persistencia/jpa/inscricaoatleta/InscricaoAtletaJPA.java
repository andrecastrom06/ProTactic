package dev.com.protactic.infraestrutura.persistencia.jpa.inscricaoatleta;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.IdClass; // 1. Importante para Chave Composta

@Entity(name = "InscricaoAtleta")
@Table(name = "InscricaoAtleta") // O nome da tabela DEVE ser igual ao do seu SQL
@IdClass(InscricaoAtletaPK.class) // 2. Diz ao JPA para usar a nossa classe PK
public class InscricaoAtletaJPA {

    // 3. Mapeamos os dois campos da Chave Primária
    @Id
    private String atleta;
    
    @Id
    private String competicao;

    // 4. Campos restantes (mapeando para snake_case)
    @Column(name = "elegivel_para_jogos")
    private boolean elegivelParaJogos;
    
    private boolean inscrito;
    
    @Column(name = "mensagem_erro")
    private String mensagemErro;

    // Construtor vazio obrigatório para o JPA
    public InscricaoAtletaJPA() {}

    // --- Getters e Setters ---
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