package dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.entidade;

import java.util.Date;
import java.util.Objects;

public class Proposta {
    private int id;

    private Integer propositorId; 
    private Integer receptorId;   
    private Integer jogadorId;    

    private String status;
    private double valor;
    private Date data; 

    
    public Proposta(int id, Integer propositorId, Integer receptorId, Integer jogadorId, String status, double valor, Date data) {
        this.id = id;
        this.propositorId = propositorId;
        this.receptorId = receptorId;
        this.jogadorId = jogadorId;
        this.status = status;
        this.valor = valor;
        this.data = data;
    }
    public Proposta() {
    }

    
    public Proposta(Integer propositorId) {
        this.propositorId = propositorId;
    }

    public Proposta(Integer propositorId, Integer receptorId, Integer jogadorId, double valor, Date data) {
        this.propositorId = propositorId;
        this.receptorId = receptorId;
        this.jogadorId = jogadorId;
        this.valor = valor;
        this.data = data;
        this.status = "PENDENTE"; 
    }

    

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proposta proposta = (Proposta) o;
        if (this.id == 0 || proposta.id == 0) return false;
        return id == proposta.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}