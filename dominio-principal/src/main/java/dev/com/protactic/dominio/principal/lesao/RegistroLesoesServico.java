package dev.com.protactic.dominio.principal.lesao;

public class RegistroLesoesServico {

    private final RegistroLesoesRepository repo;

    public RegistroLesoesServico(RegistroLesoesRepository repo) {
        this.repo = repo;
    }

    public void registrarLesao(String atletaId, int grau) {
        if (!repo.contratoAtivo(atletaId)) {
            throw new IllegalArgumentException("Contrato inativo impede o registro de lesão");
        }
        var grauAtivo = repo.grauLesaoAtiva(atletaId);
        if (grauAtivo.isPresent()) {
            throw new IllegalStateException("Finalize a recuperação da lesão ativa de grau " + grauAtivo.get());
        }

        repo.salvarLesaoAtiva(atletaId, grau);
        repo.atualizarStatusAtleta(atletaId, "Lesionado (grau " + grau + ")");
        repo.atualizarDisponibilidade(atletaId, "indisponível");
    }

    public void cadastrarPlanoRecuperacao(String atletaId, int dias) {
        if (repo.grauLesaoAtiva(atletaId).isEmpty()) {
            throw new IllegalStateException("É preciso ter lesão registrada para planejar recuperação");
        }
        repo.salvarPlanoDias(atletaId, dias);
        repo.atualizarPermissaoTreino(atletaId, "limitada");
    }
}