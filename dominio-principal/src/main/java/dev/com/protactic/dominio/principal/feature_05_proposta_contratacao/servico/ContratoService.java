package dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.servico;

import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.entidade.Contrato;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.repositorio.ContratoRepository;

public class ContratoService {

    private final ContratoRepository contratoRepo;

    public ContratoService(ContratoRepository contratoRepo) {
        this.contratoRepo = contratoRepo;
    }

    public Contrato renovarContrato(Integer contratoId, int novaDuracaoMeses, double novoSalario, String novoStatus) throws Exception {
        
        Contrato contrato = contratoRepo.buscarPorId(contratoId);
        if (contrato == null) {
            throw new Exception("Contrato com ID " + contratoId + " não encontrado.");
        }

        contrato.setDuracaoMeses(novaDuracaoMeses);
        contrato.setSalario(novoSalario);
        contrato.setStatus(novoStatus); 

        contratoRepo.salvar(contrato);
        
        return contrato;
    }
}