package dev.com.protactic.mocks;



import dev.com.protactic.dominio.principal.Contrato;
import dev.com.protactic.dominio.principal.cadastroAtleta.IContratoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementação Mock (em memória) do IContratoRepository para testes.
 */
public class ContratoMock implements IContratoRepository {

    // Simula uma tabela de banco de dados (thread-safe)
    private static final Map<Integer, Contrato> tabelaContratos = new ConcurrentHashMap<>();
    // Simula o auto-incremento do ID
    private static final AtomicInteger sequenceId = new AtomicInteger(1);

    @Override
    public void salvar(Contrato contrato) {
        if (contrato == null) {
            return;
        }
        
        // Se for um contrato novo (id=0), gera um novo ID
        if (contrato.getId() == 0) {
            int novoId = sequenceId.getAndIncrement();
            contrato.setId(novoId);
        }
        
        // Salva ou atualiza o contrato no "banco" (Map)
        tabelaContratos.put(contrato.getId(), contrato);
    }

    @Override
    public Contrato buscarPorId(Integer id) {
        if (id == null) {
            return null;
        }
        // Retorna uma cópia para simular a busca no banco (opcional, mas boa prática)
        Contrato original = tabelaContratos.get(id);
        if (original != null) {
            // Simples clone (pode ser mais robusto se necessário)
            Contrato copia = new Contrato(original.getId(), original.getDuracaoMeses(), original.getSalario(), original.getStatus(), original.getClubeId());
            return copia;
        }
        return null;
    }

    @Override
    public List<Contrato> listarTodos() {
        return new ArrayList<>(tabelaContratos.values());
    }
    
    /**
     * Helper para limpar o mock entre testes
     */
    public static void limparMock() {
        tabelaContratos.clear();
        sequenceId.set(1);
    }
}