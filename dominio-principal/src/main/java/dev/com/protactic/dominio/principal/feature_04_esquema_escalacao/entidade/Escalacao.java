package dev.com.protactic.dominio.principal.feature_04_esquema_escalacao.entidade;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.entidade.Partida;
import dev.com.protactic.dominio.principal.feature_04_esquema_escalacao.iterator.JogadorIterator; // Importação adicionada
import dev.com.protactic.dominio.principal.feature_04_esquema_escalacao.iterator.EscalacaoIterator; // Importação adicionada

public class Escalacao {
    private int id;
    private Partida partida;
    private String esquema;
    private Jogador[] jogadores; // Estrutura de dados interna (Array)

    public Escalacao(int id, Partida partida, String esquema, Jogador[] jogadores) {
        this.id = id;
        this.partida = partida;
        this.esquema = esquema;
        this.jogadores = jogadores;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Partida getPartida() { return partida; }
    public void setPartida(Partida partida) { this.partida = partida; }

    public String getEsquema() { return esquema; }
    public void setEsquema(String esquema) { this.esquema = esquema; }

    public Jogador[] getJogadores() { return jogadores; }
    public void setJogadores(Jogador[] jogadores) { this.jogadores = jogadores; }
    
    /**
     * Implementa o método de fábrica para criar o Iterador.
     * O cliente interage apenas com o retorno desta interface.
     */
    public JogadorIterator criarIterator() {
        // A Escalacao sabe como criar o Iterador específico para sua estrutura interna (Array).
        return new EscalacaoIterator(this.jogadores);
    }
}