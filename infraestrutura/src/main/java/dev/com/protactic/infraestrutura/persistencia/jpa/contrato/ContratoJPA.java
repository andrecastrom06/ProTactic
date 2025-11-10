package dev.com.protactic.infraestrutura.persistencia.jpa.contrato;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity(name = "Contrato")
@Table(name = "Contrato") 
public class ContratoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "duracao_meses")
    private int duracaoMeses;
    
    private double salario;
    private String status;
    
    @Column(name = "id_clube")
    private Integer clubeId; 

    public ContratoJPA() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getDuracaoMeses() { return duracaoMeses; }
    public void setDuracaoMeses(int duracaoMeses) { this.duracaoMeses = duracaoMeses; }
    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getClubeId() { return clubeId; }
    public void setClubeId(Integer clubeId) { this.clubeId = clubeId; }
}