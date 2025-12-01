package dev.com.protactic.mocks;



import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.entidade.Contrato;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.repositorio.ContratoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ContratoMock implements ContratoRepository {

    private static final Map<Integer, Contrato> tabelaContratos = new ConcurrentHashMap<>();
    private static final AtomicInteger sequenceId = new AtomicInteger(1);

    @Override
    public void salvar(Contrato contrato) {
        if (contrato == null) {
            return;
        }
        
        if (contrato.getId() == 0) {
            int novoId = sequenceId.getAndIncrement();
            contrato.setId(novoId);
        }
        
        tabelaContratos.put(contrato.getId(), contrato);
    }

    @Override
    public Contrato buscarPorId(Integer id) {
        if (id == null) {
            return null;
        }
        Contrato original = tabelaContratos.get(id);
        if (original != null) {
            Contrato copia = new Contrato(original.getId(), original.getDuracaoMeses(), original.getSalario(), original.getStatus(), original.getClubeId());
            return copia;
        }
        return null;
    }

    @Override
    public List<Contrato> listarTodos() {
        return new ArrayList<>(tabelaContratos.values());
    }
    
    public static void limparMock() {
        tabelaContratos.clear();
        sequenceId.set(1);
    }
}