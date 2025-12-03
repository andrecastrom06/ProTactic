package dev.com.protactic.infraestrutura.persistencia.jpa.escalacao;

import dev.com.protactic.aplicacao.principal.escalacao.EscalacaoRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.escalacao.EscalacaoResumo;
import dev.com.protactic.dominio.principal.feature_04_esquema_escalacao.repositorio.EscalacaoRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.jogador.JogadorJPA;
import dev.com.protactic.infraestrutura.persistencia.jpa.jogador.JogadorRepositorySpringData;
import dev.com.protactic.infraestrutura.persistencia.jpa.partida.PartidaJPA;
import dev.com.protactic.infraestrutura.persistencia.jpa.partida.PartidaRepositorySpringData;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EscalacaoRepositoryImpl implements EscalacaoRepository, EscalacaoRepositorioAplicacao {

    private final EscalacaoRepositorySpringData escalacaoRepository;
    private final PartidaRepositorySpringData partidaRepository;
    private final JogadorRepositorySpringData jogadorRepository;

    public EscalacaoRepositoryImpl(EscalacaoRepositorySpringData escalacaoRepository,
                                   PartidaRepositorySpringData partidaRepository,
                                   JogadorRepositorySpringData jogadorRepository) {
        this.escalacaoRepository = escalacaoRepository;
        this.partidaRepository = partidaRepository;
        this.jogadorRepository = jogadorRepository;
    }

    @Override
    public void salvarJogadorNaEscalacao(String jogoData, String nomeJogador, Integer clubeId) {
        Objects.requireNonNull(jogoData, "Data do jogo é obrigatória");
        Objects.requireNonNull(nomeJogador, "Nome do jogador é obrigatório");

        // 1. Converter String para LocalDate
        LocalDate data = converterData(jogoData);

        // 2. Buscar a Partida
        PartidaJPA partida = partidaRepository.findByDataJogo(data)
                .orElseThrow(() -> new IllegalArgumentException("Partida não encontrada para a data: " + jogoData));

        // 3. Buscar o Jogador
        JogadorJPA jogador = jogadorRepository.findByNome(nomeJogador)
                .orElseThrow(() -> new IllegalArgumentException("Jogador não encontrado: " + nomeJogador));

        // 4. Buscar ou Criar Escalação
        EscalacaoJPA escalacao = escalacaoRepository.findByPartidaIdAndClubeId(partida.getId(), clubeId)
                .orElse(new EscalacaoJPA());

        if (escalacao.getId() == 0) {
            escalacao.setPartidaId(partida.getId());
            escalacao.setClubeId(clubeId);
            escalacao.setEsquema("4-4-2"); 
        }

        // 5. Adicionar na vaga livre
        adicionarJogadorNaVagaLivre(escalacao, jogador.getId());

        // 6. Salvar
        escalacaoRepository.save(escalacao);
    }

    @Override
    public List<String> obterEscalacaoPorData(String jogoData, Integer clubeId) {
        LocalDate data = converterData(jogoData);

        // Busca Partida -> Se não existe, retorna vazio
        Optional<PartidaJPA> partidaOpt = partidaRepository.findByDataJogo(data);
        if (partidaOpt.isEmpty()) return Collections.emptyList();

        // Busca Escalação usando o ID da partida
        Optional<EscalacaoJPA> escalacaoOpt = escalacaoRepository.findByPartidaIdAndClubeId(partidaOpt.get().getId(), clubeId);
        if (escalacaoOpt.isEmpty()) return Collections.emptyList();

        // Busca os nomes dos jogadores baseados nos IDs salvos na escalação
        return extrairNomesDeEscalacao(escalacaoOpt.get());
    }

    @Override
    public List<EscalacaoResumo> pesquisarResumosPorData(String jogoData, Integer clubeId) {
        // Implementação simplificada: retorna lista vazia ou adapta conforme sua classe EscalacaoResumo
        // Se precisares retornar dados reais aqui, terás de converter EscalacaoJPA para EscalacaoResumo manualmente
        return Collections.emptyList(); 
    }

    // --- Métodos Auxiliares ---

    private LocalDate converterData(String dataString) {
        try {
            return LocalDate.parse(dataString);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de data inválido. Use YYYY-MM-DD. Recebido: " + dataString);
        }
    }

    private void adicionarJogadorNaVagaLivre(EscalacaoJPA e, Integer jogadorId) {
        if (isJogadorJaEscalado(e, jogadorId)) {
            return; // Já está lá, não faz nada
        }

        if (e.getIdJogador1() == null) e.setIdJogador1(jogadorId);
        else if (e.getIdJogador2() == null) e.setIdJogador2(jogadorId);
        else if (e.getIdJogador3() == null) e.setIdJogador3(jogadorId);
        else if (e.getIdJogador4() == null) e.setIdJogador4(jogadorId);
        else if (e.getIdJogador5() == null) e.setIdJogador5(jogadorId);
        else if (e.getIdJogador6() == null) e.setIdJogador6(jogadorId);
        else if (e.getIdJogador7() == null) e.setIdJogador7(jogadorId);
        else if (e.getIdJogador8() == null) e.setIdJogador8(jogadorId);
        else if (e.getIdJogador9() == null) e.setIdJogador9(jogadorId);
        else if (e.getIdJogador10() == null) e.setIdJogador10(jogadorId);
        else if (e.getIdJogador11() == null) e.setIdJogador11(jogadorId);
        else throw new IllegalStateException("A escalação já está cheia (11 jogadores).");
    }

    private boolean isJogadorJaEscalado(EscalacaoJPA e, Integer id) {
        return Objects.equals(e.getIdJogador1(), id) || Objects.equals(e.getIdJogador2(), id) ||
               Objects.equals(e.getIdJogador3(), id) || Objects.equals(e.getIdJogador4(), id) ||
               Objects.equals(e.getIdJogador5(), id) || Objects.equals(e.getIdJogador6(), id) ||
               Objects.equals(e.getIdJogador7(), id) || Objects.equals(e.getIdJogador8(), id) ||
               Objects.equals(e.getIdJogador9(), id) || Objects.equals(e.getIdJogador10(), id) ||
               Objects.equals(e.getIdJogador11(), id);
    }

    private List<String> extrairNomesDeEscalacao(EscalacaoJPA e) {
        List<Integer> ids = new ArrayList<>();
        if (e.getIdJogador1() != null) ids.add(e.getIdJogador1());
        if (e.getIdJogador2() != null) ids.add(e.getIdJogador2());
        if (e.getIdJogador3() != null) ids.add(e.getIdJogador3());
        if (e.getIdJogador4() != null) ids.add(e.getIdJogador4());
        if (e.getIdJogador5() != null) ids.add(e.getIdJogador5());
        if (e.getIdJogador6() != null) ids.add(e.getIdJogador6());
        if (e.getIdJogador7() != null) ids.add(e.getIdJogador7());
        if (e.getIdJogador8() != null) ids.add(e.getIdJogador8());
        if (e.getIdJogador9() != null) ids.add(e.getIdJogador9());
        if (e.getIdJogador10() != null) ids.add(e.getIdJogador10());
        if (e.getIdJogador11() != null) ids.add(e.getIdJogador11());

        if (ids.isEmpty()) return Collections.emptyList();

        return jogadorRepository.findAllById(ids).stream()
                .map(JogadorJPA::getNome)
                .collect(Collectors.toList());
    }
}