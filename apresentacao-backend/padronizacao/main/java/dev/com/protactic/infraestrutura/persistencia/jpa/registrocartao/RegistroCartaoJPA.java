package dev.com.protactic.infraestrutura.persistencia.jpa.registrocartao;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity(name = "RegistroCartao")
@Table(name = "registro_cartao") 
public class RegistroCartaoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_jogador")
    private Integer idJogador; 

    private String tipo;

    public RegistroCartaoJPA() {}
    
    public RegistroCartaoJPA(Integer idJogador, String tipo) {
        this.idJogador = idJogador;
        this.tipo = tipo;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdJogador() { return idJogador; }
    public void setIdJogador(Integer idJogador) { this.idJogador = idJogador; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}