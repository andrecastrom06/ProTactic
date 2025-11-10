package dev.com.protactic.infraestrutura.persistencia.jpa.inscricaoatleta;

import dev.com.protactic.dominio.principal.InscricaoAtleta;
import dev.com.protactic.dominio.principal.registroInscricaoAtleta.RegistroInscricaoRepository;
import dev.com.protactic.infraestrutura.persistencia.jpa.JpaMapeador;
import dev.com.protactic.aplicacao.principal.inscricaoatleta.InscricaoAtletaRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.inscricaoatleta.InscricaoAtletaResumo;


import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class RegistroInscricaoRepositoryImpl implements RegistroInscricaoRepository, InscricaoAtletaRepositorioAplicacao {

    private final RegistroInscricaoRepositorySpringData repositoryJPA;
    private final JpaMapeador mapeador;

    public RegistroInscricaoRepositoryImpl(RegistroInscricaoRepositorySpringData repositoryJPA, JpaMapeador mapeador) {
        this.repositoryJPA = repositoryJPA;
        this.mapeador = mapeador;
    }

    
    @Override
    public void salvar(InscricaoAtleta inscricao) {
        Objects.requireNonNull(inscricao, "A InscricaoAtleta a ser salva não pode ser nula.");
        InscricaoAtletaJPA jpa = mapeador.map(inscricao, InscricaoAtletaJPA.class);
        Objects.requireNonNull(jpa, "O resultado do mapeamento de InscricaoAtleta para JPA não pode ser nulo.");
        repositoryJPA.save(jpa);
    }

    @Override
    public InscricaoAtleta buscarPorAtletaECompeticao(String atleta, String competicao) {
        InscricaoAtletaPK pk = new InscricaoAtletaPK(atleta, competicao);
        return repositoryJPA.findById(pk)
                .map(jpa -> mapeador.map(jpa, InscricaoAtleta.class))
                .orElse(null);
    }

    @Override
    public List<InscricaoAtleta> listarTodas() {
        return repositoryJPA.findAll().stream()
                .map(jpa -> mapeador.map(jpa, InscricaoAtleta.class))
                .collect(Collectors.toList());
    }


    @Override
    public List<InscricaoAtletaResumo> pesquisarResumos() {
        return repositoryJPA.findAllBy();
    }

    @Override
    public List<InscricaoAtletaResumo> pesquisarResumosPorAtleta(String atleta) {
        Objects.requireNonNull(atleta, "O nome do Atleta não pode ser nulo.");
        return repositoryJPA.findByAtleta(atleta);
    }

    @Override
    public List<InscricaoAtletaResumo> pesquisarResumosPorCompeticao(String competicao) {
        Objects.requireNonNull(competicao, "O nome da Competição não pode ser nulo.");
        return repositoryJPA.findByCompeticao(competicao);
    }
    
}