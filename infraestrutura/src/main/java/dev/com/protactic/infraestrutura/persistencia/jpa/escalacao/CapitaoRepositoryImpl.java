package dev.com.protactic.infraestrutura.persistencia.jpa.escalacao;

import dev.com.protactic.dominio.principal.Capitao;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.capitao.CapitaoRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.clube.ClubeJPA;
import dev.com.protactic.infraestrutura.persistencia.jpa.clube.ClubeRepositorySpringData;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;

import org.springframework.stereotype.Component;
import java.util.Objects;
import java.util.Optional;

@Component
public class CapitaoRepositoryImpl implements CapitaoRepository {

    private final ClubeRepositorySpringData clubeRepositoryJPA;
    private final JogadorRepository jogadorRepository; 

    public CapitaoRepositoryImpl(ClubeRepositorySpringData clubeRepositoryJPA, 
                                 JogadorRepository jogadorRepository) {
        this.clubeRepositoryJPA = clubeRepositoryJPA;
        this.jogadorRepository = jogadorRepository;
    }

    
    @Override
    public void salvarCapitao(Capitao capitao) {
        Objects.requireNonNull(capitao, "O Capitão a ser salvo não pode ser nulo.");
        Objects.requireNonNull(capitao.getClubeId(), "O ClubeId do Capitão não pode ser nulo.");
        
        Jogador novoCapitao = capitao.getJogador();
        Objects.requireNonNull(novoCapitao, "O Jogador do Capitão não pode ser nulo.");
        Integer novoCapitaoId = novoCapitao.getId();
        Integer clubeId = capitao.getClubeId();

        Optional<ClubeJPA> clubeOptional = clubeRepositoryJPA.findById(clubeId); 
        if (clubeOptional.isEmpty()) {
            throw new RuntimeException("Clube não encontrado para salvar o capitão: " + clubeId);
        }
        ClubeJPA clubeJPA = clubeOptional.get();

        Integer antigoCapitaoId = clubeJPA.getCapitaoId();

        clubeJPA.setCapitaoId(novoCapitaoId);
        clubeRepositoryJPA.save(clubeJPA);

        if (antigoCapitaoId != null && !antigoCapitaoId.equals(novoCapitaoId)) {
            Jogador capitaoAntigo = jogadorRepository.buscarPorId(antigoCapitaoId);
            if (capitaoAntigo != null) {
                capitaoAntigo.setCapitao(false);
                jogadorRepository.salvar(capitaoAntigo);
            }
        }

        novoCapitao.setCapitao(true);
        jogadorRepository.salvar(novoCapitao);
        
    }

    @Override
    public Capitao buscarCapitaoPorClube(Integer clubeId) {
        Objects.requireNonNull(clubeId, "O ID do Clube não pode ser nulo.");

        Optional<ClubeJPA> clubeOptional = clubeRepositoryJPA.findById(clubeId);
        if (clubeOptional.isEmpty()) {
            return null; 
        }
        
        Integer capitaoJogadorId = clubeOptional.get().getCapitaoId();
        if (capitaoJogadorId == null) {
            return null; // Clube não tem capitão
        }

        Jogador jogador = jogadorRepository.buscarPorId(capitaoJogadorId);
        if (jogador == null) {
            throw new RuntimeException("Jogador (Capitão) não encontrado: " + capitaoJogadorId);
        }

        return new Capitao(jogador);
    }
}