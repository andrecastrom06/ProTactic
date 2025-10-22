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

    Optional<Integer> grauLesaoAtiva(String atletaId);

    String lesaoStatus(String atletaId);

    void salvarLesaoAtiva(String atletaId, int grau);

    void encerrarLesaoAtiva(String atletaId);
    Optional<Integer> planoDias(String atletaId);
    void salvarPlanoDias(String atletaId, int dias);
    void limpar();
}