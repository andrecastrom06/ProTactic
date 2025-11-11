package dev.com.protactic.infraestrutura.persistencia.jpa.registrocartao;

import dev.com.protactic.dominio.principal.RegistroCartao;
import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.RegistroCartoesRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.jogador.JogadorJPA;
import dev.com.protactic.infraestrutura.persistencia.jpa.jogador.JogadorRepositorySpringData;
import dev.com.protactic.aplicacao.principal.registrocartao.RegistroCartaoRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.registrocartao.RegistroCartaoResumo;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
// (INÍCIO DA CORREÇÃO 1) Adicionamos a interface da Aplicação de volta
public class RegistroCartoesRepositoryImpl implements RegistroCartoesRepository, RegistroCartaoRepositorioAplicacao { 

    private final RegistroCartoesRepositorySpringData cartaoRepositoryJPA;
    private final JogadorRepositorySpringData jogadorRepositoryJPA;
    
    public RegistroCartoesRepositoryImpl(
            RegistroCartoesRepositorySpringData cartaoRepositoryJPA,
            JogadorRepositorySpringData jogadorRepositoryJPA) {
        this.cartaoRepositoryJPA = cartaoRepositoryJPA;
        this.jogadorRepositoryJPA = jogadorRepositoryJPA;
    }

    // --- Métodos do Domínio (já corrigidos) ---
    
    @Override
    public void salvarCartao(RegistroCartao cartao) {
        Objects.requireNonNull(cartao, "O RegistroCartao não pode ser nulo.");
        Objects.requireNonNull(cartao.getAtleta(), "O nome do Atleta não pode ser nulo.");
        Integer idJogador = buscarIdJogadorPeloNome(cartao.getAtleta());
        RegistroCartaoJPA jpa = new RegistroCartaoJPA(idJogador, cartao.getTipo());
        cartaoRepositoryJPA.save(jpa);
    }

    @Override
    public List<RegistroCartao> buscarCartoesPorAtleta(String atleta) {
        Objects.requireNonNull(atleta, "O nome do Atleta não pode ser nulo.");
        Integer idJogador = buscarIdJogadorPeloNome(atleta);
        List<RegistroCartaoJPA> listaJPA = cartaoRepositoryJPA.findByIdJogador(idJogador);
        return listaJPA.stream()
                .map(jpa -> new RegistroCartao(jpa.getId().intValue(), atleta, jpa.getTipo()))
                .collect(Collectors.toList());
    }

    @Override
    public void limparCartoes(String atleta) {
        Objects.requireNonNull(atleta, "O nome do Atleta não pode ser nulo.");
        Integer idJogador = buscarIdJogadorPeloNome(atleta);
        cartaoRepositoryJPA.deleteByIdJogador(idJogador);
    }

    // --- (INÍCIO DA CORREÇÃO 2) Implementação dos Métodos da Aplicação ---
    
    @Override
    public List<RegistroCartaoResumo> pesquisarResumos() {
        // Chama a nova query com JOIN
        return cartaoRepositoryJPA.findAllResumos();
    }

    @Override
    public List<RegistroCartaoResumo> pesquisarResumosPorAtleta(String atleta) {
        Objects.requireNonNull(atleta, "O nome do Atleta não pode ser nulo.");
        
        // 1. TRADUZIR: Encontra o ID do jogador pelo nome
        Integer idJogador = buscarIdJogadorPeloNome(atleta);
        
        // 2. BUSCAR: Chama a nova query com JOIN usando o ID
        return cartaoRepositoryJPA.findAllResumosByIdJogador(idJogador);
    }

    @Override
    public List<RegistroCartaoResumo> pesquisarResumosPorTipo(String tipo) {
        Objects.requireNonNull(tipo, "O Tipo do cartão não pode ser nulo.");
        // Chama a nova query com JOIN
        return cartaoRepositoryJPA.findResumosByTipo(tipo);
    }

    // --- Método Auxiliar "Tradutor" (já corrigido) ---
    
    private Integer buscarIdJogadorPeloNome(String nomeAtleta) {
        return jogadorRepositoryJPA.findByNomeIgnoreCase(nomeAtleta)
    
                .stream()
                .findFirst()
                .map(JogadorJPA::getId) 
                .orElseThrow(() -> new RuntimeException(
                        "Jogador não encontrado com o nome: " + nomeAtleta));
    }
}