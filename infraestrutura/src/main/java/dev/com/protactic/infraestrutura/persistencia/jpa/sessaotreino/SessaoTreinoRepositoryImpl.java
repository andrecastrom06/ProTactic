package dev.com.protactic.infraestrutura.persistencia.jpa.sessaotreino;

import dev.com.protactic.dominio.principal.SessaoTreino;
import dev.com.protactic.dominio.principal.Partida;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.treinoTatico.SessaoTreinoRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador;
import dev.com.protactic.infraestrutura.persistencia.jpa.partida.PartidaRepositorySpringData;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;

import dev.com.protactic.aplicacao.principal.sessaotreino.SessaoTreinoRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.sessaotreino.SessaoTreinoResumo;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Component
public class SessaoTreinoRepositoryImpl implements SessaoTreinoRepository, SessaoTreinoRepositorioAplicacao {

    private final SessaoTreinoRepositorySpringData repositoryJPA;
    private final JpaMapeador mapeador;
    private final JogadorRepository jogadorRepository;
    private final PartidaRepositorySpringData partidaRepositoryJPA;

    public SessaoTreinoRepositoryImpl(SessaoTreinoRepositorySpringData repositoryJPA, 
                                      JpaMapeador mapeador,
                                      JogadorRepository jogadorRepository,
                                      PartidaRepositorySpringData partidaRepositoryJPA) {
        this.repositoryJPA = repositoryJPA;
        this.mapeador = mapeador;
        this.jogadorRepository = jogadorRepository;
        this.partidaRepositoryJPA = partidaRepositoryJPA;
    }

    
    @Override
    public void salvar(SessaoTreino sessao) {
        Objects.requireNonNull(sessao, "A SessaoTreino a ser salva não pode ser nula.");
        SessaoTreinoJPA jpa = mapeador.map(sessao, SessaoTreinoJPA.class);
        Objects.requireNonNull(jpa, "O resultado do mapeamento de SessaoTreino para JPA não pode ser nulo.");
        repositoryJPA.save(jpa);
    }

    @Override
    public List<SessaoTreino> listarPorPartida(String partidaNome) {
        Objects.requireNonNull(partidaNome, "O 'partidaNome' não pode ser nulo.");
        List<SessaoTreinoJPA> jpaList = repositoryJPA.findByPartidaNome(partidaNome);
        return jpaList.stream()
                .map(this::converterParaDominio)
                .collect(Collectors.toList());
    }

    private SessaoTreino converterParaDominio(SessaoTreinoJPA jpa) {

        if (jpa == null) return null;
        Integer partidaId = jpa.getPartidaId();
        Objects.requireNonNull(partidaId, "O ID da Partida na SessaoTreinoJPA não pode ser nulo.");
        Partida partida = partidaRepositoryJPA.findById(partidaId)
                .map(partidaJPA -> mapeador.map(partidaJPA, Partida.class))
                .orElse(null); 
        if (partida == null) {
            throw new RuntimeException("Partida não encontrada para a Sessão de Treino: " + jpa.getId());
        }
        List<Jogador> convocados = new ArrayList<>();
        if (jpa.getConvocadosIds() != null) {
            for (Integer jogadorId : jpa.getConvocadosIds()) {
                Jogador j = jogadorRepository.buscarPorId(jogadorId);
                if (j != null) {
                    convocados.add(j);
                }
            }
        }
        SessaoTreino dominio = new SessaoTreino(jpa.getNome(), partida);
        for (Jogador j : convocados) {
            dominio.adicionarConvocado(j);
        }
        return dominio;
    }

    @Override
    public List<SessaoTreinoResumo> pesquisarResumos() {
        return repositoryJPA.findAllBy();
    }

    @Override
    public List<SessaoTreinoResumo> pesquisarResumosPorPartida(Integer partidaId) {
        Objects.requireNonNull(partidaId, "O ID da Partida não pode ser nulo.");
        return repositoryJPA.findByPartidaId(partidaId);
    }

    @Override
    public List<SessaoTreinoResumo> pesquisarResumosPorConvocado(Integer jogadorId) {
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        return repositoryJPA.findByConvocadosIdsContaining(jogadorId);
    }
    
}