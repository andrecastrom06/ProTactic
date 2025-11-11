package dev.com.protactic.infraestrutura.persistencia.jpa.escalacao;

// 1. Importa os "contratos" e "entidades" do DOMÍNIO
import dev.com.protactic.dominio.principal.Capitao;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.capitao.CapitaoRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.clube.ClubeJPA;
import dev.com.protactic.infraestrutura.persistencia.jpa.clube.ClubeRepositorySpringData;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;

// 2. Importa as classes de infraestrutura
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

    // --- Implementação dos métodos da interface ---
    
    @Override
    public void salvarCapitao(Capitao capitao) {
        Objects.requireNonNull(capitao, "O Capitão a ser salvo não pode ser nulo.");
        Objects.requireNonNull(capitao.getClubeId(), "O ClubeId do Capitão não pode ser nulo.");
        
        // --- (INÍCIO DA CORREÇÃO) ---

        // 1. Pega o Novo Capitão
        Jogador novoCapitao = capitao.getJogador();
        Objects.requireNonNull(novoCapitao, "O Jogador do Capitão não pode ser nulo.");
        Integer novoCapitaoId = novoCapitao.getId();
        Integer clubeId = capitao.getClubeId();

        // 2. Busca o Clube
        Optional<ClubeJPA> clubeOptional = clubeRepositoryJPA.findById(clubeId); 
        if (clubeOptional.isEmpty()) {
            throw new RuntimeException("Clube não encontrado para salvar o capitão: " + clubeId);
        }
        ClubeJPA clubeJPA = clubeOptional.get();

        // 3. Pega o ID do Capitão Antigo (ANTES de o sobrescrever)
        Integer antigoCapitaoId = clubeJPA.getCapitaoId();

        // 4. Atualiza o Clube com o NOVO Capitão
        clubeJPA.setCapitaoId(novoCapitaoId);
        clubeRepositoryJPA.save(clubeJPA);

        // 5. Remove a braçadeira do Capitão Antigo (se ele existir)
        if (antigoCapitaoId != null && !antigoCapitaoId.equals(novoCapitaoId)) {
            Jogador capitaoAntigo = jogadorRepository.buscarPorId(antigoCapitaoId);
            if (capitaoAntigo != null) {
                capitaoAntigo.setCapitao(false);
                jogadorRepository.salvar(capitaoAntigo);
            }
        }

        // 6. Define o Novo Capitão no objeto Jogador
        novoCapitao.setCapitao(true);
        jogadorRepository.salvar(novoCapitao);
        
        // --- (FIM DA CORREÇÃO) ---
    }

    @Override
    public Capitao buscarCapitaoPorClube(Integer clubeId) {
        Objects.requireNonNull(clubeId, "O ID do Clube não pode ser nulo.");

        // 1. Busca a entidade JPA do Clube
        Optional<ClubeJPA> clubeOptional = clubeRepositoryJPA.findById(clubeId);
        if (clubeOptional.isEmpty()) {
            return null; 
        }
        
        // 2. Pega o ID do jogador que é o capitão
        Integer capitaoJogadorId = clubeOptional.get().getCapitaoId();
        if (capitaoJogadorId == null) {
            return null; // Clube não tem capitão
        }

        // 3. Usa o REPOSITÓRIO DO DOMÍNIO para buscar o Jogador
        Jogador jogador = jogadorRepository.buscarPorId(capitaoJogadorId);
        if (jogador == null) {
            throw new RuntimeException("Jogador (Capitão) não encontrado: " + capitaoJogadorId);
        }

        // 4. Constrói e retorna o objeto de domínio 'Capitao'
        return new Capitao(jogador);
    }
}