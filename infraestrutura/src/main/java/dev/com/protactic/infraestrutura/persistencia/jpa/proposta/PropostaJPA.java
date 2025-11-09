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
@Table(name = "Proposta") // 1. O nome da tabela DEVE ser igual ao do seu SQL
public class PropostaJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    // 2. Mapeia os nomes do Java (camelCase) para os nomes do SQL (snake_case)
    @Column(name = "id_propositor")
    private Integer propositorId; 
    
    @Column(name = "id_receptor")
    private Integer receptorId;   
    
    @Column(name = "id_jogador")
    private Integer jogadorId;    

    private String status;
    private double valor;
    
    // 3. A coluna que acabámos de adicionar ao SQL
    @Temporal(TemporalType.TIMESTAMP) // Garante que a data e hora sejam salvas
    private Date data; 

    // Construtor vazio obrigatório para o JPA
    public PropostaJPA() {}

    // --- Getters e Setters ---
    // (O ModelMapper e o JPA precisam de todos eles)

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