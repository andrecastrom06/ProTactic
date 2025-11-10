package dev.com.protactic.infraestrutura.persistencia.jpa.contrato;

import dev.com.protactic.dominio.principal.Contrato;
import dev.com.protactic.dominio.principal.cadastroAtleta.ContratoRepository;

import dev.com.protactic.aplicacao.principal.contrato.ContratoRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.contrato.ContratoResumo;

import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ContratoRepositoryImpl implements ContratoRepository, ContratoRepositorioAplicacao {

    private final ContratoRepositorySpringData repositoryJPA;
    private final JpaMapeador mapeador;

    public ContratoRepositoryImpl(ContratoRepositorySpringData repositoryJPA, JpaMapeador mapeador) {
        this.repositoryJPA = repositoryJPA;
        this.mapeador = mapeador;
    }
    
    @Override
    public void salvar(Contrato contrato) {
        Objects.requireNonNull(contrato, "O Contrato a ser salvo não pode ser nulo.");
        
        ContratoJPA contratoJPA = mapeador.map(contrato, ContratoJPA.class);
        
        Objects.requireNonNull(contratoJPA, "O resultado do mapeamento de Contrato para JPA não pode ser nulo.");

        ContratoJPA entidadeSalva = repositoryJPA.save(contratoJPA);

        contrato.setId(entidadeSalva.getId());
    }

    @Override
    public Contrato buscarPorId(Integer id) {
        Objects.requireNonNull(id, "O ID do Contrato a ser buscado não pode ser nulo.");
        return repositoryJPA.findById(id)
                .map(jpa -> mapeador.map(jpa, Contrato.class))
                .orElse(null);
    }

    @Override
    public List<Contrato> listarTodos() {
        return repositoryJPA.findAll().stream()
                .map(jpa -> mapeador.map(jpa, Contrato.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ContratoResumo> pesquisarResumos() {
        return repositoryJPA.findAllBy();
    }

    @Override
    public List<ContratoResumo> pesquisarResumosPorClube(Integer clubeId) {
        return repositoryJPA.findByClubeId(clubeId);
    }
}