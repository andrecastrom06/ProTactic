package dev.com.protactic.mocks;

import java.util.ArrayList;
import java.util.List;

import dev.com.protactic.dominio.principal.feature_06_inscricao_competicao.entidade.InscricaoAtleta;
import dev.com.protactic.dominio.principal.feature_06_inscricao_competicao.repositorio.RegistroInscricaoRepository;

public class RegistroInscricaoMock implements RegistroInscricaoRepository {

    private final List<InscricaoAtleta> banco = new ArrayList<>();

    @Override
    public void salvar(InscricaoAtleta inscricao) {
        banco.add(inscricao);
    }

    @Override
    public InscricaoAtleta buscarPorAtletaECompeticao(String atleta, String competicao) {
        return banco.stream()
                .filter(i -> i.getAtleta().equals(atleta) && i.getCompeticao().equals(competicao))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<InscricaoAtleta> listarTodas() {
        return new ArrayList<>(banco);
    }
}
