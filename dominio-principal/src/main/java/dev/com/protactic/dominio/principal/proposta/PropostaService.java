package dev.com.protactic.dominio.principal.proposta;

import java.util.Date;

import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Proposta;

public class PropostaService {
    private final PropostaRepository propostaRepository;

    public PropostaService(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    public Proposta criarProposta(Jogador jogador, Clube clube, Date data) throws Exception {
        if (jogador.getContrato() != null && "ATIVO".equals(jogador.getContrato().getStatus())) {
            Clube clubeAtual = jogador.getContrato().getClube();

            if (clubeAtual.equals(clube)) {
                throw new Exception("Jogador já possui contrato ativo com o clube.");
            }

            if (data == null || !estaDentroDaJanelaDeTransferencia(data)) {
                throw new Exception("Jogador não pode ser contratado fora do prazo de transferência.");
            }
        }

        Proposta proposta = new Proposta(jogador, clube, data);
        return propostaRepository.saveProposta(proposta);
    }


    private boolean estaDentroDaJanelaDeTransferencia(Date data) {
        @SuppressWarnings("deprecation")
        int mes = data.getMonth() + 1; 
        return (mes == 1 || mes == 2 || mes == 6 || mes == 7);
    }
}