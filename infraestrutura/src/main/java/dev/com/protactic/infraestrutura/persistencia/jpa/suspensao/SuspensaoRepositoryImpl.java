package dev.com.protactic.infraestrutura.persistencia.jpa.suspensao;

import dev.com.protactic.dominio.principal.Suspensao;
import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.SuspensaoRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.jogador.JogadorJPA;
import dev.com.protactic.infraestrutura.persistencia.jpa.jogador.JogadorRepositorySpringData;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SuspensaoRepositoryImpl implements SuspensaoRepository {

    private final SuspensaoRepositorySpringData suspensaoJPA;
    private final JogadorRepositorySpringData jogadorJPA;

    public SuspensaoRepositoryImpl(
            SuspensaoRepositorySpringData suspensaoJPA, 
            JogadorRepositorySpringData jogadorJPA) {
        this.suspensaoJPA = suspensaoJPA;
        this.jogadorJPA = jogadorJPA;
    }

    @Override
    public void salvarOuAtualizar(Suspensao suspensao) {
        Integer idJogador = buscarIdJogadorPeloNome(suspensao.getAtleta());

        SuspensaoJPA jpa = suspensaoJPA.findByIdJogador(idJogador)
                .orElse(new SuspensaoJPA()); 

        jpa.setIdJogador(idJogador);
        jpa.setSuspenso(suspensao.isSuspenso());
        jpa.setAmarelo(suspensao.getAmarelo());
        jpa.setVermelho(suspensao.getVermelho());

        suspensaoJPA.save(jpa);
    }

    @Override
    public Optional<Suspensao> buscarPorAtleta(String atleta) {
        Integer idJogador = buscarIdJogadorPeloNome(atleta);
        
        return suspensaoJPA.findByIdJogador(idJogador)
                .map(jpa -> new Suspensao( 
                        jpa.getId(),
                        atleta, 
                        jpa.isSuspenso(),
                        jpa.getAmarelo(),
                        jpa.getVermelho()
                ));
    }

    private Integer buscarIdJogadorPeloNome(String nomeAtleta) {
        return jogadorJPA.findByNomeIgnoreCase(nomeAtleta) 
                .stream()
                .findFirst()
                .map(JogadorJPA::getId) 
                .orElseThrow(() -> new RuntimeException(
                        "Jogador não encontrado com o nome: " + nomeAtleta));
    }
}