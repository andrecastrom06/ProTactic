package dev.com.protactic.infraestrutura.persistencia.jpa.premiacao;

import jakarta.persistence.*; // Usando * para garantir todos os imports
import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "Premiacao")
@Table(name = "Premiacao") 
public class PremiacaoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_jogador") 
    private Integer jogadorId;

    private String nome;

    @Column(name = "data_premiacao")
    @Temporal(TemporalType.DATE) 
    private Date dataPremiacao;

    @Column(name = "valor")
    private BigDecimal valor;
 
    public PremiacaoJPA() {}

    public PremiacaoJPA(Integer id, Integer jogadorId, String nome, Date dataPremiacao, BigDecimal valor) {
        this.id = id;
        this.jogadorId = jogadorId;
        this.nome = nome;
        this.dataPremiacao = dataPremiacao;
        this.valor = valor;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getJogadorId() { return jogadorId; }
    public void setJogadorId(Integer jogadorId) { this.jogadorId = jogadorId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Date getDataPremiacao() { return dataPremiacao; }
    public void setDataPremiacao(Date dataPremiacao) { this.dataPremiacao = dataPremiacao; }
    
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}