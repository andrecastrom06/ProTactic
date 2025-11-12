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

    // --- (INÍCIO DA CORREÇÃO) ---
    @Override
    public Jogador buscarPorNome(String nome) {
        Objects.requireNonNull(nome, "O Nome do Jogador a ser buscado não pode ser nulo.");
        
        // 1. Agora o 'repositoryJPA.findByNome' (do SpringData) retorna uma LISTA
        return repositoryJPA.findByNomeIgnoreCase(nome)
                .stream()
                .findFirst() // 2. Nós pegamos apenas o *primeiro* resultado da lista
                .map(jpa -> mapeador.map(jpa, Jogador.class)) // 3. Mapeamos
                .orElse(null); // 4. Se a lista estiver vazia, retornamos nulo
    }
    // --- (FIM DA CORREÇÃO) ---

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

    @Override
    public List<Jogador> findByNomeIgnoreCase(String nome) {
        
        // 1. Chama o método do Spring Data (que já existe e funciona)
        List<JogadorJPA> jpaList = repositoryJPA.findByNomeIgnoreCase(nome);

        // 2. Mapeia a lista de JPA para uma lista de Domínio
        return jpaList.stream()
                .map(jpa -> mapeador.map(jpa, Jogador.class))
                .collect(Collectors.toList());
    }

}