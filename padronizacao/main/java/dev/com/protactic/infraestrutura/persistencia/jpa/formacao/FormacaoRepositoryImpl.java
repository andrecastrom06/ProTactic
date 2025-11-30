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
    public FormacaoResumo salvar(Integer partidaId, String esquema, List<Integer> jogadoresIds, Integer clubeId) {
        // CORREÇÃO: Busca se já existe formacao DESTE CLUBE para esta partida
        Optional<EscalacaoJPA> existente = repositoryJPA.findByPartidaIdAndClubeId(partidaId, clubeId);
        
        EscalacaoJPA escalacao = existente.orElse(new EscalacaoJPA());
        
        escalacao.setPartidaId(partidaId);
        escalacao.setEsquema(esquema);
        escalacao.setClubeId(clubeId); // <--- AQUI O ID É FINALMENTE SALVO
        
        preencherJogadores(escalacao, jogadoresIds);
        
        EscalacaoJPA salva = repositoryJPA.save(escalacao);
        
        return repositoryJPA.findResumoById(salva.getId())
                .orElseThrow(() -> new RuntimeException("Erro ao recuperar resumo após salvar."));
    }

    @Override
    public FormacaoResumo editar(Integer formacaoId, Integer partidaId, String esquema, List<Integer> jogadoresIds, Integer clubeId) throws Exception {
        EscalacaoJPA formacaoExistente = repositoryJPA.findById(formacaoId)
            .orElseThrow(() -> new Exception("Formação não encontrada."));
            
        // Segurança adicional: verificar se a formação pertence ao clube
        if (formacaoExistente.getClubeId() != null && !formacaoExistente.getClubeId().equals(clubeId)) {
             throw new Exception("Você não tem permissão para editar esta formação.");
        }
            
        formacaoExistente.setPartidaId(partidaId);
        formacaoExistente.setEsquema(esquema);
        formacaoExistente.setClubeId(clubeId); // Garante atualização
        
        preencherJogadores(formacaoExistente, jogadoresIds);
        
        EscalacaoJPA salva = repositoryJPA.save(formacaoExistente);
        
        return repositoryJPA.findResumoById(salva.getId())
                .orElseThrow(() -> new RuntimeException("Erro ao recuperar resumo após editar."));
    }

    private void preencherJogadores(EscalacaoJPA escalacao, List<Integer> jogadoresIds) {
        escalacao.setIdJogador1(null); escalacao.setIdJogador2(null); 
        escalacao.setIdJogador3(null); escalacao.setIdJogador4(null);
        escalacao.setIdJogador5(null); escalacao.setIdJogador6(null);
        escalacao.setIdJogador7(null); escalacao.setIdJogador8(null);
        escalacao.setIdJogador9(null); escalacao.setIdJogador10(null);
        escalacao.setIdJogador11(null);

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