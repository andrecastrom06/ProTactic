package dev.com.protactic.dominio.principal.lesao;

import java.util.Optional;

public interface RegistroLesoesRepository {
    void cadastrarAtleta(String atletaId);
    void definirContratoAtivo(String atletaId, boolean ativo);
    boolean contratoAtivo(String atletaId);
    void atualizarStatusAtleta(String atletaId, String status);          
    String statusAtleta(String atletaId);
    void atualizarDisponibilidade(String atletaId, String disponibilidade);  
    String disponibilidadeAtleta(String atletaId);
    void atualizarPermissaoTreino(String atletaId, String permissao);  
    String permissaoTreino(String atletaId);

    // --- Lesão ativa (somente o que a feature precisa) ---
    /** @return grau da lesão ativa, se existir */
    Optional<Integer> grauLesaoAtiva(String atletaId);

    /** @return "ativa" se houver lesão ativa; "encerrada" ou "nenhuma" caso contrário (implementação decide) */
    String lesaoStatus(String atletaId);

    /** Cria/substitui a lesão ativa para o atleta, com status "ativa". */
    void salvarLesaoAtiva(String atletaId, int grau);

    void encerrarLesaoAtiva(String atletaId);
    Optional<Integer> planoDias(String atletaId);
    void salvarPlanoDias(String atletaId, int dias);
    void limpar();
}