package dev.com.protactic.dominio.principal.lesao;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Lesao;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import java.util.Objects;
import java.util.Optional;

public class RegistroLesoesServico {

    private final RegistroLesoesRepository repo;
    private final JogadorRepository jogadorRepository;
    private final LesaoRepository lesaoRepository;    

    public RegistroLesoesServico(RegistroLesoesRepository repo, 
                                 JogadorRepository jogadorRepository, 
                                 LesaoRepository lesaoRepository) { 
        this.repo = repo;
        this.jogadorRepository = jogadorRepository;
        this.lesaoRepository = lesaoRepository;    
    }

    
    public void registrarLesaoPorId(Integer jogadorId, int grau) throws Exception {
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        Jogador jogador = buscarJogadorOuLancarExcecao(jogadorId);
        
        this.registrarLesao(jogador.getNome(), grau);
    }
    
    public void cadastrarPlanoRecuperacaoPorId(Integer jogadorId, String tempo, String plano) throws Exception {
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        Jogador jogador = buscarJogadorOuLancarExcecao(jogadorId);

        Optional<Lesao> lesaoOpt = lesaoRepository.buscarAtivaPorJogadorId(jogador.getId());
        if (lesaoOpt.isEmpty()) {
            throw new Exception("Nenhuma lesão ativa encontrada para o jogador " + jogador.getNome());
        }
        
        Lesao lesao = lesaoOpt.get();
        lesao.setTempo(tempo);
        lesao.setPlano(plano);
        lesaoRepository.salvar(lesao);

        this.cadastrarPlanoRecuperacao(jogador.getNome(), 0); 
    }
    
    public void encerrarRecuperacaoPorId(Integer jogadorId) throws Exception {
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        Jogador jogador = buscarJogadorOuLancarExcecao(jogadorId);
        
        this.encerrarRecuperacao(jogador.getNome());
    }
    
    private Jogador buscarJogadorOuLancarExcecao(Integer jogadorId) throws Exception {
        Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
        if (jogador == null) {
            throw new Exception("Jogador com ID " + jogadorId + " não encontrado.");
        }
        return jogador;
    }

    public void registrarLesao(String atletaId, int grau) {
        if (!repo.contratoAtivo(atletaId)) {
            throw new IllegalArgumentException("Contrato inativo impede o registro de lesão");
        }
        var grauAtivo = repo.grauLesaoAtiva(atletaId);
        if (grauAtivo.isPresent()) {
            throw new IllegalStateException("Finalize a recuperação da lesão ativa de grau " + grauAtivo.get());
        }

        repo.salvarLesaoAtiva(atletaId, grau);
        repo.atualizarStatusAtleta(atletaId, "Lesionado (grau " + grau + ")");
        repo.atualizarDisponibilidade(atletaId, "indisponível");
    }

    public void cadastrarPlanoRecuperacao(String atletaId, int dias) {
        if (repo.grauLesaoAtiva(atletaId).isEmpty()) {
            throw new IllegalStateException("É preciso ter lesão registrada para planejar recuperação");
        }
    
        if (dias > 0) {
             repo.salvarPlanoDias(atletaId, dias);
        }
        repo.atualizarPermissaoTreino(atletaId, "limitada");
    }

    public void encerrarRecuperacao(String atletaId) {
        repo.encerrarLesaoAtiva(atletaId); 
        repo.atualizarStatusAtleta(atletaId, "Saudável");
        repo.atualizarDisponibilidade(atletaId, "Disponível");
        repo.atualizarPermissaoTreino(atletaId, "Disponível");
    }
}