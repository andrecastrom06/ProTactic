package dev.com.protactic.dominio.principal.registroInscricaoAtleta;

import dev.com.protactic.dominio.principal.InscricaoAtleta;

public class RegistroInscricaoService {

    private final RegistroInscricaoRepository repository;

    public RegistroInscricaoService(RegistroInscricaoRepository repository) {
        this.repository = repository;
    }

    public InscricaoAtleta registrarInscricao(String atleta, int idade, boolean contratoAtivo, String competicao) {
        InscricaoAtleta inscricao = new InscricaoAtleta(atleta, competicao);

        if (idade < 16) {
            inscricao.setInscrito(false);
            inscricao.setMensagemErro("Jogador menor de 16 anos não pode ser inscrito");
        } else if (!contratoAtivo) {
            inscricao.setInscrito(false);
            inscricao.setMensagemErro("Jogador sem contrato ativo não pode ser inscrito");
        } else {
            inscricao.setInscrito(true);
            inscricao.setElegivelParaJogos(true);
        }

        repository.salvar(inscricao);
        return inscricao;
    }

    public RegistroInscricaoRepository getRepository() {
        return repository;
    }
}
