package com.protactic.protactic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Empresario")
public class Empresario {

    @Id
    @Column(name = "fk_Pessoa_CPF")
    private String pessoaCpf;

    private String agencia;

    @Column(name = "Registro_FIFA", unique = true)
    private String registroFifa;

    public String getPessoaCpf() {
        return pessoaCpf;
    }

    public void setPessoaCpf(String pessoaCpf) {
        this.pessoaCpf = pessoaCpf;
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
}