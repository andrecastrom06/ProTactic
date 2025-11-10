package dev.com.protactic.infraestrutura.persistencia.jpa.jogador;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador;
import dev.com.protactic.aplicacao.principal.jogador.JogadorRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.jogador.JogadorResumo;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JogadorRepositoryImpl implements JogadorRepository, JogadorRepositorioAplicacao {

    private final JogadorRepositorySpringData repositoryJPA;
    private final JpaMapeador mapeador;

    public JogadorRepositoryImpl(JogadorRepositorySpringData repositoryJPA, JpaMapeador mapeador) {
        this.repositoryJPA = repositoryJPA;
        this.mapeador = mapeador;
    }
    
    @Override
    public void salvar(Jogador jogador) {
        Objects.requireNonNull(jogador, "O Jogador a ser salvo não pode ser nulo.");
        JogadorJPA jogadorJPA = mapeador.map(jogador, JogadorJPA.class);
        Objects.requireNonNull(jogadorJPA, "O resultado do mapeamento de Jogador para JPA não pode ser nulo.");
        repositoryJPA.save(jogadorJPA);
    }

    @Override
    public Jogador buscarPorNome(String nome) {
        Objects.requireNonNull(nome, "O Nome do Jogador a ser buscado não pode ser nulo.");
        return repositoryJPA.findByNome(nome)
                .map(jpa -> mapeador.map(jpa, Jogador.class))
                .orElse(null);
    }

    @Override
    public boolean existe(String nome) {
        Objects.requireNonNull(nome, "O Nome do Jogador a ser verificado não pode ser nulo.");
        return repositoryJPA.existsByNome(nome);
    }

    @Override
    public List<Jogador> listarTodos() {
        return repositoryJPA.findAll().stream()
                .map(jpa -> mapeador.map(jpa, Jogador.class))
                .collect(Collectors.toList());
    }

    @Override
    public Jogador buscarPorId(Integer id) {
        Objects.requireNonNull(id, "O ID do Jogador a ser buscado não pode ser nulo.");
        return repositoryJPA.findById(id)
                .map(jpa -> mapeador.map(jpa, Jogador.class))
                .orElse(null);
    }
    
    @Override
    public List<JogadorResumo> pesquisarResumos() {
        // 5. Simplesmente chamamos o método do Spring Data
        return repositoryJPA.findAllBy();
    }

    @Override
    public List<JogadorResumo> pesquisarResumosPorClube(Integer clubeId) {
        return repositoryJPA.findByClubeId(clubeId);
    }

}