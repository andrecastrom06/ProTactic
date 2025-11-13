package dev.com.protactic.infraestrutura.persistencia.jpa.fisico;

import dev.com.protactic.dominio.principal.Jogador; // Importa a entidade de domínio
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

@Entity(name = "Fisico")
@Table(name = "Fisico") 
public class FisicoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_jogador")
    private Integer jogadorId; // Mapeado a partir de Jogador jogador

    private String nome;
    private String musculo;
    private String intensidade;
    private String descricao;

    @Column(name = "data_inicio")
    @Temporal(TemporalType.DATE)
    private Date dataInicio;

    @Column(name = "data_fim")
    @Temporal(TemporalType.DATE)
    private Date dataFim;
    private String status;

    // Construtor vazio para JPA
    public FisicoJPA() {}

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Integer getJogadorId() { return jogadorId; }
    public void setJogadorId(Integer jogadorId) { this.jogadorId = jogadorId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getMusculo() { return musculo; }
    public void setMusculo(String musculo) { this.musculo = musculo; }
    public String getIntensidade() { return intensidade; }
    public void setIntensidade(String intensidade) { this.intensidade = intensidade; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Date getDataInicio() { return dataInicio; }
    public void setDataInicio(Date dataInicio) { this.dataInicio = dataInicio; }
    public Date getDataFim() { return dataFim; }
    public void setDataFim(Date dataFim) { this.dataFim = dataFim; }
    public String getStatus() { return status; } 
    public void setStatus(String status) { this.status = status; }
}