package dev.com.protactic.infraestrutura.persistencia.jpa.nota;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.IdClass; 
import java.math.BigDecimal;

@Entity(name = "Nota")
@Table(name = "Nota") 
@IdClass(NotaPK.class) 
public class NotaJPA {

    @Id
    @Column(name = "jogo_id")
    private String jogoId;
    
    @Id
    @Column(name = "jogador_id")
    private String jogadorId;

    private BigDecimal nota;
    
    @Column(columnDefinition = "TEXT")
    private String observacao;

    public NotaJPA() {}

    public String getJogoId() { return jogoId; }
    public void setJogoId(String jogoId) { this.jogoId = jogoId; }
    public String getJogadorId() { return jogadorId; }
    public void setJogadorId(String jogadorId) { this.jogadorId = jogadorId; }
    public BigDecimal getNota() { return nota; }
    public void setNota(BigDecimal nota) { this.nota = nota; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}