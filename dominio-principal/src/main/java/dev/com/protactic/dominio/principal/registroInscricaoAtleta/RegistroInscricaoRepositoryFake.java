package dev.com.protactic.dominio.principal.registroInscricaoAtleta;

import java.util.ArrayList;
import java.util.List;

public class RegistroInscricaoRepositoryFake implements RegistroInscricaoRepository {

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
