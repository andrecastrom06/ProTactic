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
    public Optional<FormacaoResumo> buscarResumoPorPartidaId(Integer partidaId) {
        return repositoryJPA.findResumoByPartidaId(partidaId);
    }

    @Override
    public FormacaoResumo salvar(Integer partidaId, String esquema, List<Integer> jogadoresIds) {
        // Verifica se já existe
        Optional<EscalacaoJPA> existente = repositoryJPA.findByPartidaId(partidaId);
        EscalacaoJPA escalacao = existente.orElse(new EscalacaoJPA());
        
        escalacao.setPartidaId(partidaId);
        escalacao.setEsquema(esquema);
        preencherJogadores(escalacao, jogadoresIds);
        
        EscalacaoJPA salva = repositoryJPA.save(escalacao);
        
        return repositoryJPA.findResumoById(salva.getId())
                .orElseThrow(() -> new RuntimeException("Erro ao recuperar resumo após salvar."));
    }

    @Override
    public FormacaoResumo editar(Integer formacaoId, Integer partidaId, String esquema, List<Integer> jogadoresIds) throws Exception {
        EscalacaoJPA formacaoExistente = repositoryJPA.findById(formacaoId)
            .orElseThrow(() -> new Exception("Formação não encontrada."));
            
        formacaoExistente.setPartidaId(partidaId);
        formacaoExistente.setEsquema(esquema);
        preencherJogadores(formacaoExistente, jogadoresIds);
        
        
        EscalacaoJPA salva = repositoryJPA.save(formacaoExistente);
        
        return repositoryJPA.findResumoById(salva.getId())
                .orElseThrow(() -> new RuntimeException("Erro ao recuperar resumo após editar."));
    }

    private void preencherJogadores(EscalacaoJPA escalacao, List<Integer> jogadoresIds) {
        escalacao.setIdJogador1(null); escalacao.setIdJogador2(null); 
        
        if (jogadoresIds.size() > 0) escalacao.setIdJogador1(jogadoresIds.get(0));
        if (jogadoresIds.size() > 1) escalacao.setIdJogador2(jogadoresIds.get(1));
        if (jogadoresIds.size() > 2) escalacao.setIdJogador3(jogadoresIds.get(2));
        if (jogadoresIds.size() > 3) escalacao.setIdJogador4(jogadoresIds.get(3));
        if (jogadoresIds.size() > 4) escalacao.setIdJogador5(jogadoresIds.get(4));
        if (jogadoresIds.size() > 5) escalacao.setIdJogador6(jogadoresIds.get(5));
        if (jogadoresIds.size() > 6) escalacao.setIdJogador7(jogadoresIds.get(6));
        if (jogadoresIds.size() > 7) escalacao.setIdJogador8(jogadoresIds.get(7));
        if (jogadoresIds.size() > 8) escalacao.setIdJogador9(jogadoresIds.get(8));
        if (jogadoresIds.size() > 9) escalacao.setIdJogador10(jogadoresIds.get(9));
        if (jogadoresIds.size() > 10) escalacao.setIdJogador11(jogadoresIds.get(10));
    }
}