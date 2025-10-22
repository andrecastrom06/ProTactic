package dev.com.protactic.dominio.principal.dispensa;

import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.Contrato;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.ClubeRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.ContratoRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;

public class DispensaService {
    
    private final ContratoRepository contratoRepo;
    private final JogadorRepository jogadorRepo;
    private final ClubeRepository clubeRepo;

    public DispensaService(ContratoRepository contratoRepo, JogadorRepository jogadorRepo, ClubeRepository clubeRepo) {
        this.contratoRepo = contratoRepo;
        this.jogadorRepo = jogadorRepo;
        this.clubeRepo = clubeRepo;
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