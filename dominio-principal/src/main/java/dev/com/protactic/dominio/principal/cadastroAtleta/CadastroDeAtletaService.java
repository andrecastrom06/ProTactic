package dev.com.protactic.dominio.principal.cadastroAtleta;

import dev.com.protactic.dominio.principal.*; 
import java.util.Calendar;
import java.util.Date;

public class CadastroDeAtletaService {

    private final IJogadorRepository jogadorRepo;
    private final IClubeRepository clubeRepo;
    
    
    private final IContratoRepository contratoRepo; 

    public CadastroDeAtletaService(IJogadorRepository jogadorRepo, IClubeRepository clubeRepo, IContratoRepository contratoRepo) {
        this.jogadorRepo = jogadorRepo;
        this.clubeRepo = clubeRepo;
        this.contratoRepo = contratoRepo;
    }

    private boolean estaDentroDaJanela(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        int mes = cal.get(Calendar.MONTH) + 1; // 1-based
        return (mes >= 6 && mes <= 8) || (mes == 12 || mes == 1 || mes == 2);
    }

    
    public boolean contratar(Clube clubeDestino, Jogador jogador, Date data) {
        boolean janelaAberta = estaDentroDaJanela(data);

        Integer contratoAtualId = jogador.getContratoId();
        
        if (contratoAtualId != null) {
            Contrato contratoAtual = contratoRepo.buscarPorId(contratoAtualId); 
            
            if (contratoAtual != null && !contratoAtual.isExpirado()) {
                if (!janelaAberta) {
                    return false; 
                }
            }
        }

        
        Contrato novoContrato = new Contrato(clubeDestino.getId());
        
        
        contratoRepo.salvar(novoContrato); 

        jogador.setContratoId(novoContrato.getId());
        jogador.setClubeId(clubeDestino.getId());
        
        clubeDestino.adicionarJogadorId(jogador.getId());

        jogadorRepo.salvar(jogador);
        clubeRepo.salvar(clubeDestino);

        return true;
    }
}