package dev.com.protactic.aplicacao.principal.formacao;

import java.util.List;
import java.util.Optional;

public interface FormacaoRepositorioAplicacao {

    Optional<FormacaoResumo> buscarResumoPorId(Integer formacaoId);
    
    Optional<FormacaoResumo> buscarResumoPorPartidaId(Integer partidaId);

    FormacaoResumo salvar(Integer partidaId, String esquema, List<Integer> jogadoresIds, Integer clubeId);
    
    FormacaoResumo editar(Integer formacaoId, Integer partidaId, String esquema, List<Integer> jogadoresIds, Integer clubeId) throws Exception;
}