package dev.com.protactic.dominio.principal.capitao;
import dev.com.protactic.dominio.principal.Capitao;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class CapitaoService {

    private final CapitaoRepository repository;
    private final JogadorRepository jogadorRepository;

    public CapitaoService(CapitaoRepository repository, JogadorRepository jogadorRepository) {
        this.repository = repository;
        this.jogadorRepository = jogadorRepository;
    }

    public Capitao buscarCapitaoPorClube(Integer clubeId) {
        Objects.requireNonNull(clubeId, "O ID do Clube não pode ser nulo.");
        return repository.buscarCapitaoPorClube(clubeId);
    }

    public void definirCapitaoPorId(Integer jogadorId) {
        Objects.requireNonNull(jogadorId, "O ID do Jogador não pode ser nulo.");
        Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
        if (jogador == null) {
            throw new RuntimeException("Jogador não encontrado: " + jogadorId);
        }
        this.definirCapitao(jogador);
    }

    public boolean podeSerCapitao(Jogador jogador) {
        if (!jogador.isContratoAtivo()) return false;
        if (!"constante".equalsIgnoreCase(jogador.getMinutagem())) return false;
        return mesesNoClube(jogador) >= 12;
    }

    public long mesesNoClube(Jogador jogador) {
        if (jogador.getChegadaNoClube() == null) return 0;
        return ChronoUnit.MONTHS.between(jogador.getChegadaNoClube(), LocalDate.now());
    }

    public void definirCapitao(Jogador jogador) {
        jogador.setCapitao(true);
        Capitao capitao = new Capitao(jogador);
        repository.salvarCapitao(capitao);
    }

    public void definirCapitaoEntreJogadores(List<Jogador> jogadores) {
        Jogador melhor = null;
        for (Jogador j : jogadores) {
            if (!podeSerCapitao(j)) continue;
            if (melhor == null || mesesNoClube(j) > mesesNoClube(melhor)) {
                melhor = j;
            } else if (mesesNoClube(j) == mesesNoClube(melhor)) {
                melhor = null;
            }
        }
        if (melhor != null) {
            definirCapitao(melhor);
        }
    }
}