package dev.com.protactic.infraestrutura.persistencia.memoria;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import dev.com.protactic.dominio.principal.Contrato;
import dev.com.protactic.dominio.principal.Proposta;
import dev.com.protactic.dominio.principal.dispensa.ContratoRepository;
import dev.com.protactic.dominio.principal.proposta.PropostaRepository;

public class Repositorio implements ContratoRepository, PropostaRepository {

	private final Map<Integer, Contrato> armazenamentoContrato = new HashMap<>();
	private final Map<Integer, Proposta> armazenamentoProposta = new HashMap<>();

	@Override
	public Contrato saveContrato(Contrato contrato) {
		notNull(contrato, "Contrato nulo");
		armazenamentoContrato.put(contrato.getId(), contrato);
		return contrato;
	}

	@Override
	public Contrato findContratoById(int id) {
		return Optional.ofNullable(armazenamentoContrato.get(id))
				.orElseThrow(() -> new IllegalArgumentException("Contrato não encontrado com id: " + id));
	}

	@Override
	public List<Contrato> findAllContratos() {
		return List.copyOf(armazenamentoContrato.values());
	}

	@Override
	public Proposta saveProposta(Proposta proposta) {
		notNull(proposta, "Proposta nula");
		armazenamentoProposta.put(proposta.getId(), proposta);
		return proposta;
	}

	@Override
	public Proposta findPropostaById(int id) {
		return Optional.ofNullable(armazenamentoProposta.get(id))
				.orElseThrow(() -> new IllegalArgumentException("Proposta não encontrada com id: " + id));
	}

	@Override
	public List<Proposta> findAllPropostas() {
		return List.copyOf(armazenamentoProposta.values());
	}
}