package dev.com.protactic.infraestrutura.persistencia.jpa.proposta;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

@Entity(name = "Proposta")
@Table(name = "Proposta") 
public class PropostaJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "id_propositor")
    private Integer propositorId; 
    
    @Column(name = "id_receptor")
    private Integer receptorId;   
    
    @Column(name = "id_jogador")
    private Integer jogadorId;    

    private String status;
    private double valor;
    
    @Temporal(TemporalType.TIMESTAMP) 
    private Date data; 

    public PropostaJPA() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Integer getPropositorId() { return propositorId; }
    public void setPropositorId(Integer propositorId) { this.propositorId = propositorId; }
    public Integer getReceptorId() { return receptorId; }
    public void setReceptorId(Integer receptorId) { this.receptorId = receptorId; }
    public Integer getJogadorId() { return jogadorId; }
    public void setJogadorId(Integer jogadorId) { this.jogadorId = jogadorId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }
}