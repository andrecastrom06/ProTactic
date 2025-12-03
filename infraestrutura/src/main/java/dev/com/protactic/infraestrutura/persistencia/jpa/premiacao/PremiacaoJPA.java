package dev.com.protactic.infraestrutura.persistencia.jpa.premiacao;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import dev.com.protactic.infraestrutura.persistencia.jpa.jogador.JogadorJPA;

@Entity(name = "Premiacao")
@Table(name = "Premiacao") 
public class PremiacaoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jogador") 
    private JogadorJPA jogador;

    private String nome;

    @Column(name = "data_premiacao")
    @Temporal(TemporalType.DATE) 
    private Date dataPremiacao;

    @Column(name = "valor")
    private BigDecimal valor;
 
    public PremiacaoJPA() {}

    public PremiacaoJPA(Integer id, JogadorJPA jogador, String nome, Date dataPremiacao, BigDecimal valor) {
        this.id = id;
        this.jogador = jogador;
        this.nome = nome;
        this.dataPremiacao = dataPremiacao;
        this.valor = valor;
    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public JogadorJPA getJogador() { return jogador; }
    public void setJogador(JogadorJPA jogador) { this.jogador = jogador; }

    public void setJogadorId(Integer jogadorId) {
        if (jogadorId != null) {
            this.jogador = new JogadorJPA();
            this.jogador.setId(jogadorId);
        }
    }
    
    public Integer getJogadorId() {
        return (jogador != null) ? jogador.getId() : null;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Date getDataPremiacao() { return dataPremiacao; }
    public void setDataPremiacao(Date dataPremiacao) { this.dataPremiacao = dataPremiacao; }
    
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}