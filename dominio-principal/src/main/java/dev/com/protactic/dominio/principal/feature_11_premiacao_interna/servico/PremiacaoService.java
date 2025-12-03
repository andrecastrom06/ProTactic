package dev.com.protactic.dominio.principal.feature_11_premiacao_interna.servico;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;
import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.entidade.Partida;
import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.repositorio.PartidaRepository;
import dev.com.protactic.dominio.principal.feature_11_premiacao_interna.entidade.Premiacao;
import dev.com.protactic.dominio.principal.feature_11_premiacao_interna.repositorio.PremiacaoRepository;
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
    private final PartidaRepository partidaRepository;
    
    private static final BigDecimal VALOR_BASE = new BigDecimal("500.00");

    public PremiacaoService(PremiacaoRepository repository, 
                            JogadorRepository jogadorRepository,
                            PartidaRepository partidaRepository) { 
        this.repository = repository;
        this.jogadorRepository = jogadorRepository; 
        this.partidaRepository = partidaRepository;
    }
    
    public record DadosPremiacao(
        Integer jogadorId,
        String nome,
        Date dataPremiacao 
    ) {}

    public Premiacao salvarPremiacaoPorDados(DadosPremiacao dados) throws Exception {
        Objects.requireNonNull(dados.jogadorId(), "O ID do Jogador não pode ser nulo.");
        
        Jogador jogador = jogadorRepository.buscarPorId(dados.jogadorId());
        if (jogador == null) {
            throw new Exception("Jogador com ID " + dados.jogadorId() + " não encontrado.");
        }
        
        BigDecimal valorCalculado = calcularValorFinanceiro(jogador, dados.dataPremiacao());

        Premiacao novaPremiacao = new Premiacao(
            0, 
            jogador,
            dados.nome(),
            dados.dataPremiacao(),
            valorCalculado
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
        premiacao.setValor(calcularValorFinanceiro(vencedor, dataPremiacao));

        repository.salvarPremiacao(premiacao);
        return premiacao;
    }

    // --- LÓGICA FINANCEIRA ---

    private BigDecimal calcularValorFinanceiro(Jogador jogador, Date dataReferencia) {
        CalculadoraPremiacao calculadora = new PremiacaoBase(VALOR_BASE);

        // Decorator de Capitão
        if (Boolean.TRUE.equals(jogador.isCapitao())) {
            calculadora = new BonusCapitaoDecorator(calculadora);
        }
        
        // Decorator de Vitória
        if (verificarVitoriaNoMes(jogador.getClubeId(), dataReferencia)) {
            calculadora = new BonusVitoriaDecorator(calculadora);
        }

        return calculadora.calcular();
    }

    private boolean verificarVitoriaNoMes(Integer clubeId, Date data) {
        if (clubeId == null) return false;
        
        List<Partida> partidas = partidaRepository.buscarPorMesEClube(data, clubeId);
        
        for (Partida p : partidas) {
            if (isVitoria(p, clubeId)) {
                return true; 
            }
        }
        return false;
    }

    private boolean isVitoria(Partida p, Integer meuClubeId) {
        try {
            return p.obterResultadoPara(meuClubeId) == Partida.Resultado.VITORIA;
        } catch (IllegalArgumentException e) {
            return false; 
        }
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
}