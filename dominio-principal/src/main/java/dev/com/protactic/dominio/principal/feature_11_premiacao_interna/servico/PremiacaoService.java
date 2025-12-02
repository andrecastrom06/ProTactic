package dev.com.protactic.dominio.principal.feature_11_premiacao_interna.servico;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;
import dev.com.protactic.dominio.principal.feature_11_premiacao_interna.entidade.Premiacao;
import dev.com.protactic.dominio.principal.feature_11_premiacao_interna.repositorio.PremiacaoRepository;
// Importação do pacote onde ficarão os Decorators
import dev.com.protactic.dominio.principal.feature_11_premiacao_interna.decorator.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PremiacaoService {

    private final PremiacaoRepository repository;
    private final JogadorRepository jogadorRepository; 

    public PremiacaoService(PremiacaoRepository repository, JogadorRepository jogadorRepository) { 
        this.repository = repository;
        this.jogadorRepository = jogadorRepository; 
    }
    
    public record DadosPremiacao(
        Integer jogadorId,
        String nome,
        Date dataPremiacao 
    ) {}

    // --- MÉTODOS EXISTENTES (Mantidos) ---

    public Premiacao salvarPremiacaoPorDados(DadosPremiacao dados) throws Exception {
        Objects.requireNonNull(dados.jogadorId(), "O ID do Jogador não pode ser nulo.");
        
        Jogador jogador = jogadorRepository.buscarPorId(dados.jogadorId());
        if (jogador == null) {
            throw new Exception("Jogador com ID " + dados.jogadorId() + " não encontrado.");
        }
        
        Premiacao novaPremiacao = new Premiacao(
            0, 
            jogador,
            dados.nome(),
            dados.dataPremiacao()
        );

        return this.salvarPremiacao(novaPremiacao);
    }
    
    public Premiacao salvarPremiacao(Premiacao premiacao) {
        repository.salvarPremiacao(premiacao);
        return premiacao;
    }

    public Premiacao criarPremiacaoMensal(String nomePremiacao, Date dataPremiacao, List<Jogador> jogadores) {
        Premiacao premiacao = repository.criarPremiacao(nomePremiacao, dataPremiacao);
        Jogador vencedor = definirVencedor(jogadores);

        if (vencedor == null) {
            return null;
        }

        premiacao.setJogador(vencedor);
        repository.salvarPremiacao(premiacao);
        return premiacao;
    }

    private Jogador definirVencedor(List<Jogador> jogadores) {
        List<Jogador> candidatos = new ArrayList<>();
        for (Jogador j : jogadores) {
            if (j.getNota() >= 6) {
                candidatos.add(j);
            }
        }

        if (candidatos.isEmpty()) return null;

        double maiorNota = candidatos.stream()
                .mapToDouble(Jogador::getNota)
                .max()
                .orElse(0);

        List<Jogador> empatados = new ArrayList<>();
        for (Jogador j : candidatos) {
            if (j.getNota() == maiorNota) {
                empatados.add(j);
            }
        }

        if (maiorNota == 6.0) {
            return empatados.get(0);
        } else if (empatados.size() > 1) {
            return empatados.stream()
                    .min(Comparator.comparingDouble(Jogador::getDesvioPadrao))
                    .orElse(null);
        } else {
            return empatados.get(0);
        }
    }
    
    public boolean verificarSeVencedorTemMenorDesvio(Jogador vencedor, List<Jogador> jogadores) {
        double menorDesvio = jogadores.stream()
                .mapToDouble(Jogador::getDesvioPadrao)
                .min()
                .orElse(Double.MAX_VALUE);
        return vencedor.getDesvioPadrao() == menorDesvio;
    }

    public void calcularEregistrarPremiacaoFinanceira(Integer jogadorId, double valorBase, boolean timeVenceu, boolean ehCapitao) throws Exception {
        Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
        if (jogador == null) {
            throw new Exception("Jogador não encontrado para cálculo de premiação.");
        }

        CalculadoraPremiacao calculadora = new PremiacaoBase(valorBase);

        if (timeVenceu) {
            calculadora = new BonusVitoriaDecorator(calculadora);
        }

        if (ehCapitao) {
            calculadora = new BonusCapitaoDecorator(calculadora);
        }

        BigDecimal valorFinal = calculadora.calcular();

        Premiacao premiacao = new Premiacao(
            0,
            jogador,
            "Premiação Financeira (Base + Bônus)",
            new Date()
        );
        
        repository.salvarPremiacao(premiacao);
        
        System.out.println("Premiação financeira registrada para " + jogador.getNome() + ": R$ " + valorFinal);
    }
}