package dev.com.protactic.dominio.principal.feature_02_carga_semanal.servico;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.calculo.CalculoCargaLinear;
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.entidade.Fisico;
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.repositorio.FisicoRepository;
import dev.com.protactic.dominio.principal.feature_03_registro_lesao.entidade.Lesao;
import dev.com.protactic.dominio.principal.feature_03_registro_lesao.observer.LesaoObserver;
import dev.com.protactic.dominio.principal.feature_03_registro_lesao.servico.RegistroLesoesServico; // NOVO IMPORT

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PlanejamentoFisicoService implements LesaoObserver {

    private final FisicoRepository fisicoRepository;
    private final JogadorRepository jogadorRepository;
    private final PlanejamentoCargaSemanalService planejamentoCargaSemanalService;

    public PlanejamentoFisicoService(FisicoRepository fisicoRepository,
                                     JogadorRepository jogadorRepository,
                                     PlanejamentoCargaSemanalService planejamentoCargaSemanalService,
                                     RegistroLesoesServico registroLesoesServico) { // Parâmetro injetado
        this.fisicoRepository = fisicoRepository;
        this.jogadorRepository = jogadorRepository;
        this.planejamentoCargaSemanalService = planejamentoCargaSemanalService;

        if (registroLesoesServico != null) {
            registroLesoesServico.adicionarObserver(this);
        }
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

        planejamentoCargaSemanalService.registrarTreino(
            jogador, 
            60.0, 
            5.0, 
            new CalculoCargaLinear() 
        );

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
    
    @Override
    public void observarLesao(Lesao lesao) {
        Integer jogadorId = lesao.getJogador().getId();
        
        // Busca treinos ativos ou planejados para o jogador
        List<Fisico> treinosAtivos = fisicoRepository.buscarPorJogadorId(jogadorId);
        
        for (Fisico treino : treinosAtivos) {
            // Verifica se o treino está planeado e suspende-o
            if ("PLANEJADO".equalsIgnoreCase(treino.getStatus()) || 
                "PROTOCOLO_RETORNO".equalsIgnoreCase(treino.getStatus())) {
                
                treino.setStatus("SUSPENSO_LESAO");
                fisicoRepository.salvar(treino);
            }
        }
    }
}