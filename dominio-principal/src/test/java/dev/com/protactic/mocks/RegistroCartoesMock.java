package dev.com.protactic.mocks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.RegistroCartao;
import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.RegistroCartoesRepository;

public class RegistroCartoesMock implements RegistroCartoesRepository {

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
