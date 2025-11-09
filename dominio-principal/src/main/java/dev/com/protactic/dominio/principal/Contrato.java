package dev.com.protactic.dominio.principal;

import java.util.Objects;

public class Contrato {
    private int id;
    private int duracaoMeses;
    private double salario;
    private String status;
    
    private Integer clubeId; 
    
    public Contrato() {}

    public Contrato(int id, int duracaoMeses, double salario, String status, Integer clubeId) {
        this.id = id;
        this.duracaoMeses = duracaoMeses;
        this.salario = salario;
        this.status = status;
        this.clubeId = clubeId;
    }

    public Contrato(Integer clubeId) {
        this.clubeId = clubeId;
        this.status = "ATIVO";
    }

    public boolean isExpirado() {
        return !"ATIVO".equalsIgnoreCase(this.status);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDuracaoMeses() {
        return duracaoMeses;
    }

    public void setDuracaoMeses(int duracaoMeses) {
        this.duracaoMeses = duracaoMeses;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getClubeId() {
        return clubeId;
    }

    public void setClubeId(Integer clubeId) {
        this.clubeId = clubeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contrato contrato = (Contrato) o;
        if (this.id == 0 || contrato.id == 0) return false;
        return id == contrato.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}