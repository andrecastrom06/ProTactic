package dev.com.protactic.dominio.principal.registroCartoesSuspensoes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RegistroCartoesRepositoryFake implements RegistroCartoesRepository {

private final List<RegistroCartao> registros = new ArrayList<>();

@Override
public void salvarCartao(RegistroCartao cartao) {
    registros.add(cartao);
}

@Override
public List<RegistroCartao> buscarCartoesPorAtleta(String atleta) {
    return registros.stream()
        .filter(c -> c.getAtleta().equals(atleta))
        .collect(Collectors.toList());
}

@Override
public void limparCartoes(String atleta) {
    registros.removeIf(c -> c.getAtleta().equals(atleta));
}


}
