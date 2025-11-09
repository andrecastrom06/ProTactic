package dev.com.protactic.infraestrutura.persistencia.jpa.contrato;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity(name = "Contrato")
@Table(name = "Contrato") // 1. O nome da tabela DEVE ser igual ao do seu SQL
public class ContratoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    // 2. Mapeia os nomes do Java (camelCase) para os nomes do SQL (snake_case)
    @Column(name = "duracao_meses")
    private int duracaoMeses;
    
    private double salario;
    private String status;
    
    // 3. A coluna que acabámos de adicionar ao SQL
    @Column(name = "id_clube")
    private Integer clubeId; 

    // Construtor vazio obrigatório para o JPA
    public ContratoJPA() {}

    // --- Getters e Setters ---
    // (O ModelMapper e o JPA precisam de todos eles)

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