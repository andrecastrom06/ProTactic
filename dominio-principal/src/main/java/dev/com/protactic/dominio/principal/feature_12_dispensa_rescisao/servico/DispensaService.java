package dev.com.protactic.dominio.principal.feature_12_dispensa_rescisao.servico;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Clube;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.ClubeRepository;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.entidade.Contrato;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.repositorio.ContratoRepository;

import java.util.Objects; 

public class DispensaService {
    
    private final ContratoRepository contratoRepo;
    private final JogadorRepository jogadorRepo;
    private final ClubeRepository clubeRepo;

    public DispensaService(ContratoRepository contratoRepo, JogadorRepository jogadorRepo, ClubeRepository clubeRepo) {
        this.contratoRepo = contratoRepo;
        this.jogadorRepo = jogadorRepo;
        this.clubeRepo = clubeRepo;
    }

    public void dispensarJogadorPorId(Integer jogadorId) throws Exception {
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        
        Jogador jogador = jogadorRepo.buscarPorId(jogadorId);
        if (jogador == null) {
            throw new Exception("Jogador não encontrado: " + jogadorId);
        }
        
        this.dispensarJogador(jogador);
    }

    public void dispensarJogador(Jogador jogador) throws Exception {
        if (jogador == null) {
            throw new Exception("Jogador não pode ser nulo.");
        }
        
        if (jogador.getContratoId() == null) {
            throw new Exception("Jogador não possui contrato ativo.");
        }

        if (!jogadorSaudavel(jogador)) {
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
    }

    private boolean jogadorSaudavel(Jogador jogador) {
        return jogador.isSaudavel();
    }
}