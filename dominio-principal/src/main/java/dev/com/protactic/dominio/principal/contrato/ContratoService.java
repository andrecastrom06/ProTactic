package dev.com.protactic.dominio.principal.contrato;

import dev.com.protactic.dominio.principal.Contrato;
import dev.com.protactic.dominio.principal.cadastroAtleta.ContratoRepository;
import java.util.Objects;

/**
 * Serviço de Domínio para gerir a lógica de negócio
 * relacionada a contratos (ex: renovação).
 */
public class ContratoService {

    private final ContratoRepository contratoRepo;

    public ContratoService(ContratoRepository contratoRepo) {
        this.contratoRepo = contratoRepo;
    }

    /**
     * Implementa a história "Atualizar contratos".
     * Encontra um contrato e atualiza os seus termos.
     */
    public Contrato renovarContrato(Integer contratoId, int novaDuracaoMeses, double novoSalario, String novoStatus) throws Exception {
        
        // 1. Encontrar o contrato
        Contrato contrato = contratoRepo.buscarPorId(contratoId);
        if (contrato == null) {
            throw new Exception("Contrato com ID " + contratoId + " não encontrado.");
        }

        // 2. Atualizar os campos
        contrato.setDuracaoMeses(novaDuracaoMeses);
        contrato.setSalario(novoSalario);
        contrato.setStatus(novoStatus); // Ex: "ATIVO"

        // 3. Salvar (o repositório 'salvar' trata o UPDATE)
        contratoRepo.salvar(contrato);
        
        return contrato;
    }
}