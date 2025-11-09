package dev.com.protactic.infraestrutura.persistencia.jpa.lesao;

// 1. Importa os "contratos" e "entidades" do DOMÍNIO
import dev.com.protactic.dominio.principal.Contrato;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Lesao;
import dev.com.protactic.dominio.principal.cadastroAtleta.ContratoRepository;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.lesao.LesaoRepository;
import dev.com.protactic.dominio.principal.lesao.RegistroLesoesRepository;

// 2. Importa as classes de infraestrutura
import org.springframework.stereotype.Component;
import java.util.Objects;
import java.util.Optional;

@Component
public class RegistroLesoesRepositoryImpl implements RegistroLesoesRepository {

    // ... (injeções de dependência - permanecem iguais) ...
    private final JogadorRepository jogadorRepository;
    private final ContratoRepository contratoRepository;
    private final LesaoRepository lesaoRepository;

    public RegistroLesoesRepositoryImpl(JogadorRepository jogadorRepository,
                                        ContratoRepository contratoRepository,
                                        LesaoRepository lesaoRepository) {
        this.jogadorRepository = jogadorRepository;
        this.contratoRepository = contratoRepository;
        this.lesaoRepository = lesaoRepository;
    }
    
    // ... (salvarLesaoAtiva, encerrarLesaoAtiva, etc. permanecem iguais) ...
    @Override
    public boolean contratoAtivo(String atletaId) {
        Jogador j = buscarJogadorNN(atletaId);
        if (j.getContratoId() == null) return false;
        Contrato c = contratoRepository.buscarPorId(j.getContratoId());
        if (c == null) return false;
        return !c.isExpirado();
    }
    @Override
    public void atualizarStatusAtleta(String atletaId, String status) {
        Jogador j = buscarJogadorNN(atletaId);
        j.setStatus(status);
        jogadorRepository.salvar(j);
    }
    @Override
    public String statusAtleta(String atletaId) {
        return buscarJogadorNN(atletaId).getStatus();
    }
    @Override
    public Optional<Integer> grauLesaoAtiva(String atletaId) {
        Jogador j = buscarJogadorNN(atletaId);
        Optional<Lesao> lesaoAtiva = lesaoRepository.buscarAtivaPorJogadorId(j.getId());
        return lesaoAtiva.map(Lesao::getGrau);
    }
    @Override
    public void salvarLesaoAtiva(String atletaId, int grau) {
        Jogador j = buscarJogadorNN(atletaId);
        Lesao novaLesao = new Lesao(0, j, true, null, null, grau);
        lesaoRepository.salvar(novaLesao);
        j.setGrauLesao(grau);
        j.setSaudavel(false);
        jogadorRepository.salvar(j);
    }
    @Override
    public void encerrarLesaoAtiva(String atletaId) {
        Jogador j = buscarJogadorNN(atletaId);
        Optional<Lesao> lesaoOpt = lesaoRepository.buscarAtivaPorJogadorId(j.getId());
        if (lesaoOpt.isPresent()) {
            Lesao lesao = lesaoOpt.get();
            lesao.setLesionado(false);
            lesaoRepository.salvar(lesao);
        }
        j.setGrauLesao(-1);
        j.setSaudavel(true);
        jogadorRepository.salvar(j);
    }
    @Override
    public void salvarPlanoDias(String atletaId, int dias) {
        Jogador j = buscarJogadorNN(atletaId);
        Optional<Lesao> lesaoOpt = lesaoRepository.buscarAtivaPorJogadorId(j.getId());
        if (lesaoOpt.isEmpty()) {
            throw new IllegalStateException("Nenhuma lesão ativa encontrada para salvar o plano de dias.");
        }
        Lesao lesao = lesaoOpt.get();
        // O BDD pede 'int dias', então preenchemos o TEMPO e um PLANO genérico
        lesao.setTempo(dias + " dias"); 
        lesao.setPlano("Recuperação adaptada");
        lesaoRepository.salvar(lesao);
    }

    
    // --- (INÍCIO DA CORREÇÃO) ---
    // Mapeamos os conceitos do BDD (disponibilidade, permissaoTreino)
    // para o campo 'status' que existe no Jogador.java
    
    @Override
    public void atualizarDisponibilidade(String atletaId, String disponibilidade) {
        // O BDD diz: disponibilidade = "indisponível"
        // Mapeamos isto para o status do Jogador
        this.atualizarStatusAtleta(atletaId, disponibilidade);
    }

    @Override
    public String disponibilidadeAtleta(String atletaId) {
        // O BDD lê a disponibilidade (que é o status)
        return this.statusAtleta(atletaId);
    }

    @Override
    public void atualizarPermissaoTreino(String atletaId, String permissao) {
        // O BDD diz: permissaoTreino = "limitada"
        // Mapeamos isto para o status do Jogador
        this.atualizarStatusAtleta(atletaId, permissao);
    }

    @Override
    public String permissaoTreino(String atletaId) {
        // O BDD lê a permissão (que agora é o status)
        return this.statusAtleta(atletaId);
    }
    
    // --- (FIM DA CORREÇÃO) ---


    @Override
    public Optional<Integer> planoDias(String atletaId) {
        // O BDD pede um <Integer> mas a Entidade 'Lesao' salva
        // o plano como uma 'String' (ex: "10 dias").
        // Não podemos converter, então retornamos 'empty'.
        return Optional.empty(); 
    }

    // --- Métodos Não Usados ---
    @Override
    public void cadastrarAtleta(String atletaId) {
        throw new UnsupportedOperationException("Método 'cadastrarAtleta' não implementado.");
    }
    @Override
    public void definirContratoAtivo(String atletaId, boolean ativo) {
         Jogador j = buscarJogadorNN(atletaId);
         j.setContratoAtivo(ativo);
         jogadorRepository.salvar(j);
    }
    @Override
    public String lesaoStatus(String atletaId) {
        throw new UnsupportedOperationException("Método 'lesaoStatus' não implementado.");
    }
    @Override
    public void limpar() {
        throw new UnsupportedOperationException("Método 'limpar' não implementado.");
    }

    // --- Método Ajudante Interno ---
    private Jogador buscarJogadorNN(String atletaId) {
        Objects.requireNonNull(atletaId, "O ID (nome) do atleta não pode ser nulo.");
        Jogador jogador = jogadorRepository.buscarPorNome(atletaId);
        if (jogador == null) {
            throw new RuntimeException("Atleta não encontrado com o nome: " + atletaId);
        }
        return jogador;
    }
}