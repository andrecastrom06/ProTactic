package dev.com.protactic.dominio.principal.proposta;

import java.util.Date;

import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.Contrato;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Proposta;
import dev.com.protactic.dominio.principal.cadastroAtleta.ContratoRepository;

// Sem @Service, como você mencionou
public class PropostaService {
    
    private final PropostaRepository propostaRepo;
    private final ContratoRepository contratoRepo;

    public PropostaService(PropostaRepository propostaRepo, ContratoRepository contratoRepo) {
        this.propostaRepo = propostaRepo;
        this.contratoRepo = contratoRepo;
    }

    public Proposta criarProposta(Jogador jogador, Clube clubePropositor, double valor, Date data) throws Exception {
 
        
        Integer clubeAtualId = null;
        
        Integer contratoId = jogador.getContratoId();

        if (contratoId != null) {
            Contrato contrato = contratoRepo.buscarPorId(contratoId);

            if (contrato != null && "ATIVO".equalsIgnoreCase(contrato.getStatus())) {
                clubeAtualId = jogador.getClubeId();

                if (clubeAtualId != null && clubeAtualId.equals(clubePropositor.getId())) {
                    throw new Exception("Jogador já possui contrato ativo com o clube.");
                }

                if (data == null || !estaDentroDaJanelaDeTransferencia(data)) {
                    throw new Exception("Jogador não pode ser contratado fora do prazo de transferência.");
                }
            }
        }

        Proposta proposta = new Proposta(
            clubePropositor.getId(), 
            clubeAtualId, 
            jogador.getId(), 
            valor, 
            data
        );
      
        return propostaRepo.saveProposta(proposta);
    }

    private boolean estaDentroDaJanelaDeTransferencia(Date data) {
        @SuppressWarnings("deprecation")
        int mes = data.getMonth() + 1; 
        return (mes == 1 || mes == 2 || mes == 6 || mes == 7 || mes == 11 || mes == 12);
    }
}