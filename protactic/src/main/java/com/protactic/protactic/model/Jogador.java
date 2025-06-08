package com.protactic.protactic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.math.BigDecimal;

@Entity
@Table(name = "Jogador")
public class Jogador {

    @Id
    @Column(name = "fk_Pessoa_CPF")
    private String pessoaCpf;

    @ManyToOne
    @JoinColumn(name = "fk_Empresario_CPF")
    private Empresario empresario;

    @ManyToOne
    @JoinColumn(name = "fk_Clube_CNPJ")
    private Clube clube;

    private String posicao;
    private BigDecimal altura;
    private BigDecimal peso;

    @Column(name = "Valor_estimado")
    private Float valorEstimado;

    @Column(name = "Perna_preferida")
    private String pernaPreferida;

    private String categoria;

    public String getPessoaCpf() {
        return pessoaCpf;
    }

    public void setPessoaCpf(String pessoaCpf) {
        this.pessoaCpf = pessoaCpf;
    }

    public Empresario getEmpresario() {
        return empresario;
    }

    public void setEmpresario(Empresario empresario) {
        this.empresario = empresario;
    }

    public Clube getClube() {
        return clube;
    }

    public void setClube(Clube clube) {
        this.clube = clube;
    }

    public String getPosicao() {
        return posicao;
    }

    public void setPosicao(String posicao) {
        this.posicao = posicao;
    }

    public BigDecimal getAltura() {
        return altura;
    }

    public void setAltura(BigDecimal altura) {
        this.altura = altura;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public Float getValorEstimado() {
        return valorEstimado;
    }

    public void setValorEstimado(Float valorEstimado) {
        this.valorEstimado = valorEstimado;
    }

    public String getPernaPreferida() {
        return pernaPreferida;
    }

    public void setPernaPreferida(String pernaPreferida) {
        this.pernaPreferida = pernaPreferida;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}