package dev.com.protactic.infraestrutura.persistencia.jpa.formacao;

import dev.com.protactic.aplicacao.principal.formacao.FormacaoRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.formacao.FormacaoResumo;
import dev.com.protactic.infraestrutura.persistencia.jpa.escalacao.EscalacaoJPA;
import dev.com.protactic.infraestrutura.persistencia.jpa.escalacao.EscalacaoRepositorySpringData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FormacaoRepositoryImpl implements FormacaoRepositorioAplicacao {

    private final EscalacaoRepositorySpringData repositoryJPA;

    public FormacaoRepositoryImpl(EscalacaoRepositorySpringData repositoryJPA) {
        this.repositoryJPA = repositoryJPA;
    }

    @Override
    public Optional<FormacaoResumo> buscarResumoPorId(Integer formacaoId) {
        return repositoryJPA.findResumoById(formacaoId);
    }

    @Override
    public FormacaoResumo salvar(Integer partidaId, String esquema, List<Integer> jogadoresIds) {
        EscalacaoJPA novaEscalacao = new EscalacaoJPA();
        novaEscalacao.setPartidaId(partidaId);
        novaEscalacao.setEsquema(esquema);
        preencherJogadores(novaEscalacao, jogadoresIds);
        
        EscalacaoJPA entidadeSalva = repositoryJPA.save(novaEscalacao);
        
        return repositoryJPA.findResumoById(entidadeSalva.getId())
            .orElseThrow(() -> new RuntimeException("Não foi possível buscar o resumo da formação salva."));
    }

    @Override
    public FormacaoResumo editar(Integer formacaoId, Integer partidaId, String esquema, List<Integer> jogadoresIds) throws Exception {
        EscalacaoJPA formacaoExistente = repositoryJPA.findById(formacaoId)
            .orElseThrow(() -> new Exception("Formação com ID " + formacaoId + " não encontrada."));

        formacaoExistente.setPartidaId(partidaId);
        formacaoExistente.setEsquema(esquema);
        preencherJogadores(formacaoExistente, jogadoresIds);

        EscalacaoJPA entidadeAtualizada = repositoryJPA.save(formacaoExistente);
        
        return repositoryJPA.findResumoById(entidadeAtualizada.getId())
             .orElseThrow(() -> new RuntimeException("Não foi possível buscar o resumo da formação editada."));
    }
    
    private void preencherJogadores(EscalacaoJPA escalacao, List<Integer> jogadoresIds) {
        escalacao.setIdJogador1(jogadoresIds.get(0));
        escalacao.setIdJogador2(jogadoresIds.get(1));
        escalacao.setIdJogador3(jogadoresIds.get(2));
        escalacao.setIdJogador4(jogadoresIds.get(3));
        escalacao.setIdJogador5(jogadoresIds.get(4));
        escalacao.setIdJogador6(jogadoresIds.get(5));
        escalacao.setIdJogador7(jogadoresIds.get(6));
        escalacao.setIdJogador8(jogadoresIds.get(7));
        escalacao.setIdJogador9(jogadoresIds.get(8));
        escalacao.setIdJogador10(jogadoresIds.get(9));
        escalacao.setIdJogador11(jogadoresIds.get(10));
    }
}