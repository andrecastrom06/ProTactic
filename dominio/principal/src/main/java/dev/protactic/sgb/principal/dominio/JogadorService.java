package dev.protactic.sgb.principal.dominio;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.Optional;

public class JogadorService {

    private final JogadorRepository jogadorRepository;
    private final ClubeRepository clubeRepository;

    public JogadorService(JogadorRepository jogadorRepository, ClubeRepository clubeRepository) {
        notNull(jogadorRepository, "O repositório de jogadores não pode ser nulo.");
        notNull(clubeRepository, "O repositório de clubes não pode ser nulo.");

        this.jogadorRepository = jogadorRepository;
        this.clubeRepository = clubeRepository;
    }

    public void dispensarJogador(String nomeJogador) throws Exception {
        notNull(nomeJogador, "O nome do jogador não pode ser nulo.");

        Optional<Jogador> jogadorOptional = jogadorRepository.findByNome(nomeJogador);

        if (jogadorOptional.isEmpty()) {
            throw new IllegalArgumentException("Jogador não encontrado.");
        }

        Jogador jogador = jogadorOptional.get();

        if (jogador.getClube() == null) {
            throw new IllegalArgumentException("Jogador não pertence a um clube.");
        }

        Clube clube = jogador.getClube();

        jogador.setClube(null);
        
        jogadorRepository.salvar(jogador);
        clubeRepository.salvar(clube);
    }
}