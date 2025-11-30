package dev.com.protactic.dominio.principal.planejamentoFisico;

import dev.com.protactic.dominio.principal.Fisico;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.planejamentoCargaSemanal.PlanejamentoCargaSemanalService;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class PlanejamentoFisicoService {

    private final FisicoRepository fisicoRepository;
    private final JogadorRepository jogadorRepository;
    private final PlanejamentoCargaSemanalService planejamentoCargaSemanalService;

    public PlanejamentoFisicoService(FisicoRepository fisicoRepository,
                                     JogadorRepository jogadorRepository,
                                     PlanejamentoCargaSemanalService planejamentoCargaSemanalService) {
        this.fisicoRepository = fisicoRepository;
        this.jogadorRepository = jogadorRepository;
        this.planejamentoCargaSemanalService = planejamentoCargaSemanalService;
    }

    public record DadosTreinoFisico(
        Integer jogadorId,
        String nome,
        String musculo,
        String intensidade,
        String descricao,
        Date dataInicio,
        Date dataFim
    ) {}

    public Fisico salvarTreinoFisico(DadosTreinoFisico formulario) throws Exception {
        Jogador jogador = buscarJogadorOuLancarExcecao(formulario.jogadorId());

        boolean aptoParaTreino = planejamentoCargaSemanalService.registrarTreino(jogador);
        if (!aptoParaTreino) {
            throw new Exception("Jogador inapto para o treino (lesionado ou sem contrato).");
        }

        Fisico novoTreino = new Fisico(
            0, 
            jogador,
            formulario.nome(),
            formulario.musculo(),
            formulario.intensidade(),
            formulario.descricao(),
            formulario.dataInicio(),
            formulario.dataFim(),
            "PLANEJADO" 
        );
       
        return fisicoRepository.salvar(novoTreino);
    }
    
    public Fisico editarTreinoFisico(Integer treinoId, DadosTreinoFisico formulario) throws Exception {
        Fisico treinoExistente = buscarTreinoOuLancarExcecao(treinoId);

        if (formulario.jogadorId() != null && !formulario.jogadorId().equals(treinoExistente.getJogador().getId())) {
             throw new Exception("Não é permitido alterar o jogador de um treino existente.");
        }

        treinoExistente.setNome(formulario.nome());
        treinoExistente.setMusculo(formulario.musculo());
        treinoExistente.setIntensidade(formulario.intensidade());
        treinoExistente.setDescricao(formulario.descricao());
        treinoExistente.setDataInicio(formulario.dataInicio());
        treinoExistente.setDataFim(formulario.dataFim());

        return fisicoRepository.salvar(treinoExistente);
    }

    public Fisico atualizarStatusTreino(Integer treinoId, String status) throws Exception {
        Fisico treinoExistente = buscarTreinoOuLancarExcecao(treinoId);
        treinoExistente.setStatus(status);
        return fisicoRepository.salvar(treinoExistente);
    }
    
    public Fisico criarProtocoloDeRetorno(DadosTreinoFisico formulario) throws Exception {
        Jogador jogador = buscarJogadorOuLancarExcecao(formulario.jogadorId());

        Fisico novoProtocolo = new Fisico(
            0, 
            jogador,
            formulario.nome(),
            formulario.musculo(),
            formulario.intensidade(),
            formulario.descricao(),
            formulario.dataInicio(),
            formulario.dataFim(),
            "PROTOCOLO_RETORNO" 
        );

        return fisicoRepository.salvar(novoProtocolo);
    }
    
    private Jogador buscarJogadorOuLancarExcecao(Integer jogadorId) throws Exception {
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
        if (jogador == null) {
            throw new Exception("Jogador com ID " + jogadorId + " não encontrado.");
        }
        return jogador;
    }
    
    private Fisico buscarTreinoOuLancarExcecao(Integer treinoId) throws Exception {
         Objects.requireNonNull(treinoId, "O ID do Treino não pode ser nulo.");
         Optional<Fisico> treinoOpt = fisicoRepository.buscarPorId(treinoId);
         if (treinoOpt.isEmpty()) {
            throw new Exception("Treino Físico com ID " + treinoId + " não encontrado.");
         }
         return treinoOpt.get();
    }
}