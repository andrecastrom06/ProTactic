package dev.protactic.sgb.principal.dominio;

public interface ClubeRepository {
    void salvar(Clube clube);

    Clube obter(ClubeId id);

    Clube buscarPorNome(String nome);
}