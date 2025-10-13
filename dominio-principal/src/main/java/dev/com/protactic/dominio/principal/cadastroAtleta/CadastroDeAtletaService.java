package dev.com.protactic.dominio.principal.cadastroAtleta;

import dev.com.protactic.dominio.principal.*;
import java.util.Calendar;
import java.util.Date;

public class CadastroDeAtletaService {

    private final IJogadorRepository jogadorRepo;
    private final IClubeRepository clubeRepo;

    public CadastroDeAtletaService(IJogadorRepository jogadorRepo, IClubeRepository clubeRepo) {
        this.jogadorRepo = jogadorRepo;
        this.clubeRepo = clubeRepo;
    }

    private boolean estaDentroDaJanela(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        int mes = cal.get(Calendar.MONTH) + 1; // 1-based
        return (mes >= 6 && mes <= 8) || (mes == 12 || mes == 1 || mes == 2);
    }

    public boolean contratar(Clube clubeDestino, Jogador jogador, Date data) {
        boolean janelaAberta = estaDentroDaJanela(data);

        // Caso 1: jogador com contrato ativo
        if (jogador.getContrato() != null && !jogador.getContrato().isExpirado()) {
            if (!janelaAberta) {
                return false; // não pode contratar fora da janela
            }
        }

        // Caso 2: jogador sem contrato ou contrato expirado
        Contrato novoContrato = new Contrato(clubeDestino);
        jogador.setContrato(novoContrato);
        clubeDestino.adicionarJogador(jogador);

        jogadorRepo.salvar(jogador);
        clubeRepo.salvar(clubeDestino);

        return true;
    }
}
