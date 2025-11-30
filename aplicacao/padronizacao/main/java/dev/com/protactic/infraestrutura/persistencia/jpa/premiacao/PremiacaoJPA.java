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
@Table(name = "Premiacao") 
public class PremiacaoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_jogador")
    private Integer jogadorId;

    private String nome;

    @Column(name = "data_premiacao")
    @Temporal(TemporalType.DATE) 
    private Date dataPremiacao;
 
    public PremiacaoJPA() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Integer getJogadorId() { return jogadorId; }
    public void setJogadorId(Integer jogadorId) { this.jogadorId = jogadorId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Date getDataPremiacao() { return dataPremiacao; }
    public void setDataPremiacao(Date dataPremiacao) { this.dataPremiacao = dataPremiacao; }
}