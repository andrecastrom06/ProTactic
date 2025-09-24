package dev.com.protactic.dominio.principal.dispensa;

import dev.com.protactic.dominio.principal.Clube;
import dev.com.protactic.dominio.principal.Contrato;
import dev.com.protactic.dominio.principal.Jogador;

public class ContratacaoServico {

    /**
     * Tenta registrar um novo atleta em um clube, seguindo as regras de negócio.
     * @param clubeDestino O clube que está tentando contratar.
     * @param jogador O atleta a ser contratado.
     * @param janelaDeTransferenciaAberta Se a janela de transferências está aberta.
     * @return {@code true} se a contratação for bem-sucedida, {@code false} caso contrário.
     */
    public boolean registrarAtleta(Clube clubeDestino, Jogador jogador, boolean janelaDeTransferenciaAberta) {
        if (jogador.getClube() == null) {
            clubeDestino.adicionarJogador(jogador);
            jogador.setContrato(new Contrato(clubeDestino)); 
            return true;
        }

        if (jogador.getClube() != null && !janelaDeTransferenciaAberta) {
            System.out.println("Falha ao contratar " + jogador.getNome() + ": A janela de transferências está fechada.");
            return false;
        }
        
        return false;
    }
}
