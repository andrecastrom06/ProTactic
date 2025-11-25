package dev.com.protactic.apresentacao.principal.feature_04_esquema_escalacao;

import java.util.List;

import dev.com.protactic.aplicacao.principal.formacao.FormacaoServicoAplicacao;
import dev.com.protactic.aplicacao.principal.formacao.FormacaoServicoAplicacao.FormacaoDados;
import dev.com.protactic.apresentacao.principal.feature_04_esquema_escalacao.FormacaoControlador.FormacaoFormulario;

/**
 * Padrão Criacional: BUILDER.
 * Constrói de forma segura e legível o objeto FormacaoDados 
 * para a camada de Aplicação a partir do Formulario da Web.
 */
public class FormacaoDadosBuilder {

    private Integer partidaId;
    private String esquema;
    private List<Integer> jogadoresIds;
    private Integer clubeId;

    public static FormacaoDadosBuilder fromFormulario(FormacaoFormulario form) {
        FormacaoDadosBuilder builder = new FormacaoDadosBuilder();
        builder.partidaId = form.partidaId();
        builder.esquema = form.esquema();
        builder.jogadoresIds = form.jogadoresIds();
        builder.clubeId = form.clubeId();
        return builder;
    }
    
    // Método que garante que um campo obrigatório foi setado (exemplo)
    public FormacaoDadosBuilder validarClubeId() {
        if (this.clubeId == null) {
            throw new IllegalArgumentException("Erro de segurança: ID do clube não informado.");
        }
        return this;
    }

    public FormacaoDados build() {
        if (esquema == null || jogadoresIds == null) {
            throw new IllegalStateException("Esquema tático e jogadores são obrigatórios.");
        }
        
        return new FormacaoServicoAplicacao.FormacaoDados(
            this.partidaId,
            this.esquema,
            this.jogadoresIds,
            this.clubeId
        );
    }
}