package dev.com.protactic.infraestrutura.persistencia.jpa.suspensao;

import dev.com.protactic.dominio.principal.feature_08_registro_cartoes.entidade.Suspensao;
import dev.com.protactic.dominio.principal.feature_08_registro_cartoes.repositorio.SuspensaoRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.jogador.JogadorJPA;
import dev.com.protactic.infraestrutura.persistencia.jpa.jogador.JogadorRepositorySpringData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public List<Suspensao> buscarSuspensosPorClube(Integer clubeId) {
        List<JogadorJPA> jogadores = jogadorJPA.findAllByClubeId(clubeId); 
        
        List<Suspensao> listaSuspensos = new ArrayList<>();

        for (JogadorJPA jogador : jogadores) {
            suspensaoJPA.findByIdJogador(jogador.getId()).ifPresent(s -> {
                if (s.isSuspenso()) {
                    listaSuspensos.add(new Suspensao(
                        s.getId(),
                        jogador.getNome(),
                        s.isSuspenso(),
                        s.getAmarelo(),
                        s.getVermelho()
                    ));
                }
            });
        }
        return listaSuspensos;
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