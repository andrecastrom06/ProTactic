package dev.com.protactic.dominio.principal.feature_03_registro_lesao.servico;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;
import dev.com.protactic.dominio.principal.feature_03_registro_lesao.entidade.Lesao;
import dev.com.protactic.dominio.principal.feature_03_registro_lesao.repositorio.LesaoRepository;
import dev.com.protactic.dominio.principal.feature_03_registro_lesao.repositorio.RegistroLesoesRepository;
import dev.com.protactic.dominio.principal.feature_03_registro_lesao.observer.LesaoObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RegistroLesoesServico {

    private final RegistroLesoesRepository repo;
    private final JogadorRepository jogadorRepository;
    private final LesaoRepository lesaoRepository;    
    
    private final List<LesaoObserver> observers; 

    public RegistroLesoesServico(RegistroLesoesRepository repo, 
                                 JogadorRepository jogadorRepository, 
                                 LesaoRepository lesaoRepository) { 
        this.repo = repo;
        this.jogadorRepository = jogadorRepository;
        this.lesaoRepository = lesaoRepository;
        this.observers = new ArrayList<>();
    }
    
    public void adicionarObserver(LesaoObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("O observer não pode ser nulo.");
        }
        observers.add(observer);
    }
    
    public void removerObserver(LesaoObserver observer) {
        observers.remove(observer);
    }
    
    private void notificarObservers(Lesao lesao) {
        for (LesaoObserver observer : observers) {
            observer.observarLesao(lesao); 
        }
    }

    public void registrarLesaoPorId(Integer jogadorId, int grau) throws Exception {
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        Jogador jogador = buscarJogadorOuLancarExcecao(jogadorId);
        
        this.registrarLesao(jogador.getNome(), grau);
        
        Optional<Lesao> lesaoOpt = lesaoRepository.buscarAtivaPorJogadorId(jogador.getId());
        if (lesaoOpt.isPresent()) {
            this.notificarObservers(lesaoOpt.get());
        }
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
        
        Optional<Lesao> lesaoOpt = lesaoRepository.buscarAtivaPorJogadorId(jogador.getId());

        this.encerrarRecuperacao(jogador.getNome());

        if (lesaoOpt.isPresent()) {
            this.notificarObservers(lesaoOpt.get());
        }
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
        repo.atualizarPermissaoTreino(atletaId, "Indisponível");
    }

    public void encerrarRecuperacao(String atletaId) {
        repo.encerrarLesaoAtiva(atletaId); 
        repo.atualizarStatusAtleta(atletaId, "Saudável");
        repo.atualizarDisponibilidade(atletaId, "Disponível");
        repo.atualizarPermissaoTreino(atletaId, "Disponível");
    }
}