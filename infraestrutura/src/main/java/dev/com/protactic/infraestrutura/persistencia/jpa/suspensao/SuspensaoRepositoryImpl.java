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
        // 1. Traduzir Nome -> ID
        Integer idJogador = buscarIdJogadorPeloNome(suspensao.getAtleta());

        // 2. Tentar buscar se o registro já existe
        SuspensaoJPA jpa = suspensaoJPA.findByIdJogador(idJogador)
                .orElse(new SuspensaoJPA()); // Se não existe, cria um novo

        // 3. Atualizar os dados do objeto JPA
        jpa.setIdJogador(idJogador);
        jpa.setSuspenso(suspensao.isSuspenso());
        jpa.setAmarelo(suspensao.getAmarelo());
        jpa.setVermelho(suspensao.getVermelho());
        // O ID (PK) será gerenciado pelo JPA (se for novo) ou mantido (se for update)

        // 4. Salvar
        suspensaoJPA.save(jpa);
    }

    @Override
    public Optional<Suspensao> buscarPorAtleta(String atleta) {
        Integer idJogador = buscarIdJogadorPeloNome(atleta);
        
        return suspensaoJPA.findByIdJogador(idJogador)
                .map(jpa -> new Suspensao( // Mapear de volta para o objeto de domínio
                        jpa.getId(),
                        atleta, 
                        jpa.isSuspenso(),
                        jpa.getAmarelo(),
                        jpa.getVermelho()
                ));
    }

    // Método auxiliar (corrigido para Ignorar Case)
    private Integer buscarIdJogadorPeloNome(String nomeAtleta) {
        return jogadorJPA.findByNomeIgnoreCase(nomeAtleta) // Usando findByNomeIgnoreCase
                .stream()
                .findFirst()
                .map(JogadorJPA::getId) 
                .orElseThrow(() -> new RuntimeException(
                        "Jogador não encontrado com o nome: " + nomeAtleta));
    }
}