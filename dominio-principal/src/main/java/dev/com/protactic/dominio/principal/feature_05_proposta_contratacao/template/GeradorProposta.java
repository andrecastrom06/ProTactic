package dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.template;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Clube;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.entidade.Proposta;
import java.math.BigDecimal;
import java.util.Date;

public abstract class GeradorProposta {

    /**
     * TEMPLATE METHOD: Define o fluxo de criação.
     * É final para que a ordem não seja alterada.
     */
    public final Proposta gerarProposta(Jogador jogador, Clube clubePropositor, Integer clubeAtualId, double valorBase, Date data) {
        
        // 1. Passo Variável: Calcula bônus (Luvas)
        BigDecimal bonus = calcularBonus(new BigDecimal(valorBase));
        
        // 2. Passo Comum: Soma valor base + bônus
        double valorFinal = new BigDecimal(valorBase).add(bonus).doubleValue();

        // 3. Passo Variável: Calcula a multa (apenas para registro/log por enquanto, 
        // já que a Proposta aparentemente só guarda o 'valor' da oferta)
        BigDecimal multa = calcularMulta(new BigDecimal(valorBase));
        System.out.println("Multa calculada para contrato futuro: " + multa);

        // 4. Passo Comum: Criação do Objeto usando o construtor existente
        return criarObjetoProposta(clubePropositor.getId(), clubeAtualId, jogador.getId(), valorFinal, data);
    }

    // Métodos que as subclasses DEVEM implementar (Hooks)
    protected abstract BigDecimal calcularBonus(BigDecimal salarioBase);
    protected abstract BigDecimal calcularMulta(BigDecimal salarioBase);

    // Método auxiliar para encapsular a chamada do construtor
    private Proposta criarObjetoProposta(Integer idClube, Integer idClubeAtual, Integer idJogador, double valor, Date data) {
        return new Proposta(idClube, idClubeAtual, idJogador, valor, data);
    }
}