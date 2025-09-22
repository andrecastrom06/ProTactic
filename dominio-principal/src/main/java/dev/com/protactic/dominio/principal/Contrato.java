package dev.com.protactic.dominio.principal;

public class Contrato {
    private int id;
    private int duracaoMeses;
    private double salario;
    private String status;

    public Contrato(int id, int duracaoMeses, double salario, String status) {
        this.id = id;
        this.duracaoMeses = duracaoMeses;
        this.salario = salario;
        this.status = status;
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
}