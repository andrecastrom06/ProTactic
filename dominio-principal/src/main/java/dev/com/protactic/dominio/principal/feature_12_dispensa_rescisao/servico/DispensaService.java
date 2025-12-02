package dev.com.protactic.dominio.principal.feature_12_dispensa_rescisao.servico;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Clube;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Usuario;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.ClubeRepository;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.entidade.Contrato;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.repositorio.ContratoRepository;

import java.util.Objects; 

// Implementa a interface para garantir compatibilidade com o Proxy
public class DispensaService implements IDispensaService {
    
    private final ContratoRepository contratoRepo;
    private final JogadorRepository jogadorRepo;
    private final ClubeRepository clubeRepo;

    public DispensaService(ContratoRepository contratoRepo, JogadorRepository jogadorRepo, ClubeRepository clubeRepo) {
        this.contratoRepo = contratoRepo;
        this.jogadorRepo = jogadorRepo;
        this.clubeRepo = clubeRepo;
    }

    // --- MÉTODOS DE COMPATIBILIDADE (Evitam erros no PropostaService) ---

    public void dispensarJogadorPorId(Integer jogadorId) throws Exception {
        this.dispensarJogadorPorId(jogadorId, null);
    }

    public void dispensarJogador(Jogador jogador) throws Exception {
        this.executarDispensa(jogador);
    }


    public void dispensarJogadorPorId(Integer jogadorId, Usuario usuarioSolicitante) throws Exception {
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        
        Jogador jogador = jogadorRepo.buscarPorId(jogadorId);
        if (jogador == null) {
            throw new Exception("Jogador não encontrado: " + jogadorId);
        }
        
        this.dispensarJogador(jogador, usuarioSolicitante);
    }

    @Override
    public void dispensarJogador(Jogador jogador, Usuario usuarioSolicitante) throws Exception {
        // Este é o método chamado pelo Proxy após a verificação de segurança.
        // O Usuario chega aqui, mas a lógica de negócio de dispensa em si 
        // não precisa dele, então apenas executamos a dispensa.
        
        if (usuarioSolicitante != null) {
            System.out.println("Dispensa autorizada por: " + usuarioSolicitante.getNome());
        }
        
        this.executarDispensa(jogador);
    }

    // --- LÓGICA CENTRAL DE DISPENSA (Privada para evitar duplicação) ---

    private void executarDispensa(Jogador jogador) throws Exception {
        if (jogador == null) {
            throw new Exception("Jogador não pode ser nulo.");
        }
        
        if (jogador.getContratoId() == null) {
            throw new Exception("Jogador não possui contrato ativo.");
        }

        if (!jogador.isSaudavel()) { // Ajustado para chamar o método direto do Jogador
            throw new Exception("Não é permitido dispensar jogadores que estão lesionados.");
        }

        Contrato contrato = contratoRepo.buscarPorId(jogador.getContratoId());
        if (contrato == null) {
            throw new Exception("Contrato não encontrado no repositório.");
        }
        contrato.setStatus("RESCINDIDO");
        contratoRepo.salvar(contrato);

        Integer clubeAntigoId = jogador.getClubeId();
        if (clubeAntigoId != null) {
            Clube clubeAntigo = clubeRepo.buscarPorId(clubeAntigoId);
            if (clubeAntigo != null) {
                clubeAntigo.removerJogadorId(jogador.getId());
                clubeRepo.salvar(clubeAntigo);
            }
        }

        jogador.setClubeId(null);
        jogador.setContratoId(null);
        jogadorRepo.salvar(jogador);
        
        System.out.println("LOG: Jogador " + jogador.getNome() + " dispensado com sucesso.");
    }
}