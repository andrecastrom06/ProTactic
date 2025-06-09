package com.protactic.protactic.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Empresario")
public class Empresario {

    @Id
    @Column(name = "fk_Pessoa_CPF")
    private String cpf;

    private String agencia;

    @Column(name = "Registro_FIFA", unique = true)
    private String registroFifa;

    @OneToOne
    @JoinColumn(name = "fk_Pessoa_CPF", referencedColumnName = "cpf", insertable = false, updatable = false)
    private Pessoa pessoa;

    // Getters e Setters

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getRegistroFifa() {
        return registroFifa;
    }

    public void setRegistroFifa(String registroFifa) {
        this.registroFifa = registroFifa;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
        if (pessoa != null) {
            this.cpf = pessoa.getCpf(); 
        }
    }
}
