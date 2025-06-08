package com.protactic.protactic.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Desempenha")
@IdClass(DesempenhaId.class)
public class Desempenha {

    @Id
    @ManyToOne
    @JoinColumn(name = "fk_Partida_ID")
    private Partida partida;

    @Id
    @ManyToOne
    @JoinColumn(name = "fk_Jogador_CPF")
    private Jogador jogador;

    private Integer gols;
    private Integer assistencias;
    private Integer passeCurtoTentados;
    private Integer passeCurtoAcertados;
    private Integer passeLongoTentados;
    private Integer passeLongoAcertados;
    private Integer cruzamentoTentados;
    private Integer cruzamentoAcertados;
    private Integer dominioTentados;
    private Integer dominioAcertados;
    private Integer finalizacaoTentadas;
    private Integer finalizacaoAcertadas;
    private Integer cabeceioTentados;
    private Integer cabeceiosAcertados;
    private Integer dribleTentados;
    private Integer dribleAcertados;
    private Integer faltasTentadas;
    private Integer faltasAcertadas;
    private BigDecimal velocidadeMedia;
    private BigDecimal velocidadeSprint;
    private BigDecimal velocidadeReacao;
    private BigDecimal impulsao;
    private Integer resistencia;
    private Integer forca;
    private Integer chuteLongeTentados;
    private Integer chuteLongeAcertados;
    private Integer interceptacaoTentadas;
    private Integer interceptacaoAcertadas;
    private Integer penaltisTentados;
    private Integer penaltisAcertados;
    private Integer desarmesTentados;
    private Integer desarmesAcertados;
    private Integer chutesGoleiroDefendidos;
    private Integer chutesGoleiroNaoDefendidos;
    private Integer lancamentoComMaoTentado;
    private Integer lancamentoComMaoAcertado;

    public Partida getPartida() {
        return partida;
    }
    public void setPartida(Partida partida) {
        this.partida = partida;
    }
    public Jogador getJogador() {
        return jogador;
    }
    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }
    public Integer getGols() {
        return gols;
    }
    public void setGols(Integer gols) {
        this.gols = gols;
    }
    public Integer getAssistencias() {
        return assistencias;
    }
    public void setAssistencias(Integer assistencias) {
        this.assistencias = assistencias;
    }
    public Integer getPasseCurtoTentados() {
        return passeCurtoTentados;
    }
    public void setPasseCurtoTentados(Integer passeCurtoTentados) {
        this.passeCurtoTentados = passeCurtoTentados;
    }
    public Integer getPasseCurtoAcertados() {
        return passeCurtoAcertados;
    }
    public void setPasseCurtoAcertados(Integer passeCurtoAcertados) {
        this.passeCurtoAcertados = passeCurtoAcertados;
    }
    public Integer getPasseLongoTentados() {
        return passeLongoTentados;
    }
    public void setPasseLongoTentados(Integer passeLongoTentados) {
        this.passeLongoTentados = passeLongoTentados;
    }
    public Integer getPasseLongoAcertados() {
        return passeLongoAcertados;
    }
    public void setPasseLongoAcertados(Integer passeLongoAcertados) {
        this.passeLongoAcertados = passeLongoAcertados;
    }
    public Integer getCruzamentoTentados() {
        return cruzamentoTentados;
    }
    public void setCruzamentoTentados(Integer cruzamentoTentados) {
        this.cruzamentoTentados = cruzamentoTentados;
    }
    public Integer getCruzamentoAcertados() {
        return cruzamentoAcertados;
    }
    public void setCruzamentoAcertados(Integer cruzamentoAcertados) {
        this.cruzamentoAcertados = cruzamentoAcertados;
    }
    public Integer getDominioTentados() {
        return dominioTentados;
    }
    public void setDominioTentados(Integer dominioTentados) {
        this.dominioTentados = dominioTentados;
    }
    public Integer getDominioAcertados() {
        return dominioAcertados;
    }
    public void setDominioAcertados(Integer dominioAcertados) {
        this.dominioAcertados = dominioAcertados;
    }
    public Integer getFinalizacaoTentadas() {
        return finalizacaoTentadas;
    }
    public void setFinalizacaoTentadas(Integer finalizacaoTentadas) {
        this.finalizacaoTentadas = finalizacaoTentadas;
    }
    public Integer getFinalizacaoAcertadas() {
        return finalizacaoAcertadas;
    }
    public void setFinalizacaoAcertadas(Integer finalizacaoAcertadas) {
        this.finalizacaoAcertadas = finalizacaoAcertadas;
    }
    public Integer getCabeceioTentados() {
        return cabeceioTentados;
    }
    public void setCabeceioTentados(Integer cabeceioTentados) {
        this.cabeceioTentados = cabeceioTentados;
    }
    public Integer getCabeceiosAcertados() {
        return cabeceiosAcertados;
    }
    public void setCabeceiosAcertados(Integer cabeceiosAcertados) {
        this.cabeceiosAcertados = cabeceiosAcertados;
    }
    public Integer getDribleTentados() {
        return dribleTentados;
    }
    public void setDribleTentados(Integer dribleTentados) {
        this.dribleTentados = dribleTentados;
    }
    public Integer getDribleAcertados() {
        return dribleAcertados;
    }
    public void setDribleAcertados(Integer dribleAcertados) {
        this.dribleAcertados = dribleAcertados;
    }
    public Integer getFaltasTentadas() {
        return faltasTentadas;
    }
    public void setFaltasTentadas(Integer faltasTentadas) {
        this.faltasTentadas = faltasTentadas;
    }
    public Integer getFaltasAcertadas() {
        return faltasAcertadas;
    }
    public void setFaltasAcertadas(Integer faltasAcertadas) {
        this.faltasAcertadas = faltasAcertadas;
    }
    public BigDecimal getVelocidadeMedia() {
        return velocidadeMedia;
    }
    public void setVelocidadeMedia(BigDecimal velocidadeMedia) {
        this.velocidadeMedia = velocidadeMedia;
    }
    public BigDecimal getVelocidadeSprint() {
        return velocidadeSprint;
    }
    public void setVelocidadeSprint(BigDecimal velocidadeSprint) {
        this.velocidadeSprint = velocidadeSprint;
    }
    public BigDecimal getVelocidadeReacao() {
        return velocidadeReacao;
    }
    public void setVelocidadeReacao(BigDecimal velocidadeReacao) {
        this.velocidadeReacao = velocidadeReacao;
    }
    public BigDecimal getImpulsao() {
        return impulsao;
    }
    public void setImpulsao(BigDecimal impulsao) {
        this.impulsao = impulsao;
    }
    public Integer getResistencia() {
        return resistencia;
    }
    public void setResistencia(Integer resistencia) {
        this.resistencia = resistencia;
    }
    public Integer getForca() {
        return forca;
    }
    public void setForca(Integer forca) {
        this.forca = forca;
    }
    public Integer getChuteLongeTentados() {
        return chuteLongeTentados;
    }
    public void setChuteLongeTentados(Integer chuteLongeTentados) {
        this.chuteLongeTentados = chuteLongeTentados;
    }
    public Integer getChuteLongeAcertados() {
        return chuteLongeAcertados;
    }
    public void setChuteLongeAcertados(Integer chuteLongeAcertados) {
        this.chuteLongeAcertados = chuteLongeAcertados;
    }
    public Integer getInterceptacaoTentadas() {
        return interceptacaoTentadas;
    }
    public void setInterceptacaoTentadas(Integer interceptacaoTentadas) {
        this.interceptacaoTentadas = interceptacaoTentadas;
    }
    public Integer getInterceptacaoAcertadas() {
        return interceptacaoAcertadas;
    }
    public void setInterceptacaoAcertadas(Integer interceptacaoAcertadas) {
        this.interceptacaoAcertadas = interceptacaoAcertadas;
    }
    public Integer getPenaltisTentados() {
        return penaltisTentados;
    }
    public void setPenaltisTentados(Integer penaltisTentados) {
        this.penaltisTentados = penaltisTentados;
    }
    public Integer getPenaltisAcertados() {
        return penaltisAcertados;
    }
    public void setPenaltisAcertados(Integer penaltisAcertados) {
        this.penaltisAcertados = penaltisAcertados;
    }
    public Integer getDesarmesTentados() {
        return desarmesTentados;
    }
    public void setDesarmesTentados(Integer desarmesTentados) {
        this.desarmesTentados = desarmesTentados;
    }
    public Integer getDesarmesAcertados() {
        return desarmesAcertados;
    }
    public void setDesarmesAcertados(Integer desarmesAcertados) {
        this.desarmesAcertados = desarmesAcertados;
    }
    public Integer getChutesGoleiroDefendidos() {
        return chutesGoleiroDefendidos;
    }
    public void setChutesGoleiroDefendidos(Integer chutesGoleiroDefendidos) {
        this.chutesGoleiroDefendidos = chutesGoleiroDefendidos;
    }
    public Integer getChutesGoleiroNaoDefendidos() {
        return chutesGoleiroNaoDefendidos;
    }
    public void setChutesGoleiroNaoDefendidos(Integer chutesGoleiroNaoDefendidos) {
        this.chutesGoleiroNaoDefendidos = chutesGoleiroNaoDefendidos;
    }
    public Integer getLancamentoComMaoTentado() {
        return lancamentoComMaoTentado;
    }
    public void setLancamentoComMaoTentado(Integer lancamentoComMaoTentado) {
        this.lancamentoComMaoTentado = lancamentoComMaoTentado;
    }
    public Integer getLancamentoComMaoAcertado() {
        return lancamentoComMaoAcertado;
    }
    public void setLancamentoComMaoAcertado(Integer lancamentoComMaoAcertado) {
        this.lancamentoComMaoAcertado = lancamentoComMaoAcertado;
    }
}