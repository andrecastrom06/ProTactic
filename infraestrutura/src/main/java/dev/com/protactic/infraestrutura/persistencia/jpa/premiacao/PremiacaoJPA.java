package dev.com.protactic.infraestrutura.persistencia.jpa.premiacao;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

@Entity(name = "Premiacao")
@Table(name = "Premiacao") // 1. O nome da tabela DEVE ser igual ao do seu SQL
public class PremiacaoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // 2. Mapeia para a coluna 'id_jogador' (o ModelMapper vai preencher isto)
    @Column(name = "id_jogador")
    private Integer jogadorId;

    private String nome;

    @Column(name = "data_premiacao")
    @Temporal(TemporalType.DATE) // Define que é apenas a Data
    private Date dataPremiacao;

    // Construtor vazio obrigatório para o JPA
    public PremiacaoJPA() {}

    // --- Getters e Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Integer getJogadorId() { return jogadorId; }
    public void setJogadorId(Integer jogadorId) { this.jogadorId = jogadorId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Date getDataPremiacao() { return dataPremiacao; }
    public void setDataPremiacao(Date dataPremiacao) { this.dataPremiacao = dataPremiacao; }
}