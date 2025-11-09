package dev.com.protactic.infraestrutura.persistencia.jpa.nota;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.IdClass; // 1. Importante para Chave Composta
import java.math.BigDecimal;

@Entity(name = "Nota")
@Table(name = "Nota") // O nome da tabela DEVE ser igual ao do seu SQL
@IdClass(NotaPK.class) // 2. Diz ao JPA para usar a nossa classe NotaPK como chave
public class NotaJPA {

    // 3. Mapeamos os dois campos da Chave Primária
    @Id
    @Column(name = "jogo_id")
    private String jogoId;
    
    @Id
    @Column(name = "jogador_id")
    private String jogadorId;

    // 4. Campos restantes
    private BigDecimal nota;
    
    @Column(columnDefinition = "TEXT") // Mapeia para o tipo TEXT do SQL
    private String observacao;

    // Construtor vazio obrigatório para o JPA
    public NotaJPA() {}

    // --- Getters e Setters ---
    public String getJogoId() { return jogoId; }
    public void setJogoId(String jogoId) { this.jogoId = jogoId; }
    public String getJogadorId() { return jogadorId; }
    public void setJogadorId(String jogadorId) { this.jogadorId = jogadorId; }
    public BigDecimal getNota() { return nota; }
    public void setNota(BigDecimal nota) { this.nota = nota; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}