package dev.com.protactic.dominio.principal;

public class Contrato {
    private int id;
    private int duracaoMeses;
    private double salario;
    private String status;
    private Clube clube;

    public Contrato(int id, int duracaoMeses, double salario, String status, Clube clube) {
        this.id = id;
        this.duracaoMeses = duracaoMeses;
        this.salario = salario;
        this.status = status;
        this.clube = clube;
    }

    public Contrato(Clube clube) {
        this.clube = clube;
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

    public Clube getClube() {
        return clube;
    }
    public void setClube(Clube clube) {
        this.clube = clube;
    }

    
}