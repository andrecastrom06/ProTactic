package dev.com.protactic.dominio.principal;

public class ContratacaoServico {

    /**
     * Tenta registrar um novo atleta em um clube, seguindo as regras de negócio.
     * @param clubeDestino O clube que está tentando contratar.
     * @param jogador O atleta a ser contratado.
     * @param janelaDeTransferenciaAberta Se a janela de transferências está aberta.
     * @return {@code true} se a contratação for bem-sucedida, {@code false} caso contrário.
     */
    public boolean registrarAtleta(Clube clubeDestino, Jogador jogador, boolean janelaDeTransferenciaAberta) {
        // REGRA 1: Se o jogador não tem clube (sem contrato), a contratação é sempre permitida.
        if (jogador.getClube() == null) {
            clubeDestino.adicionarJogador(jogador);
            // Ao adicionar, o ideal seria o clube criar um novo contrato para o jogador.
            jogador.setContrato(new Contrato(clubeDestino)); 
            return true;
        }

        // REGRA 2: Se o jogador já tem clube, a contratação só é permitida se a janela estiver aberta.
        if (jogador.getClube() != null && !janelaDeTransferenciaAberta) {
            System.out.println("Falha ao contratar " + jogador.getNome() + ": A janela de transferências está fechada.");
            return false;
        }
        
        // Se a regra 2 falhar (ou seja, a janela está fechada), a contratação não ocorre.
        return false;
    }
}
