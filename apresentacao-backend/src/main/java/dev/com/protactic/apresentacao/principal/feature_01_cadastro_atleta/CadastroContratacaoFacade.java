package dev.com.protactic.apresentacao.principal.feature_01_cadastro_atleta;

import dev.com.protactic.dominio.principal.cadastroAtleta.CadastroDeAtletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CadastroContratacaoFacade {

    @Autowired
    private CadastroDeAtletaService cadastroDeAtletaService;

    public static class ContratacaoNaoPermitidaException extends RuntimeException {
        public ContratacaoNaoPermitidaException(String message) {
            super(message);
        }
    }

    public void processarContratacao(Integer clubeId, Integer jogadorId, Date dataContratacao) throws Exception { // 💡 CORREÇÃO AQUI

        boolean resultado = cadastroDeAtletaService.contratarPorId(
            clubeId,
            jogadorId,
            dataContratacao
        ); 

        if (!resultado) {
            throw new ContratacaoNaoPermitidaException("Contratação não permitida. Verifique a janela de transferências ou a disponibilidade do atleta.");
        }
    }
}