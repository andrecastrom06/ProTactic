package dev.com.protactic.dominio.principal.registroInscricaoAtleta;

import dev.com.protactic.dominio.principal.InscricaoAtleta;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.lesao.RegistroLesoesRepository;
import java.util.Objects;

public class RegistroInscricaoService {

    private final RegistroInscricaoRepository repository;
    private final JogadorRepository jogadorRepository;
    private final RegistroLesoesRepository lesoesRepository;

    public RegistroInscricaoService(RegistroInscricaoRepository repository,
                                    JogadorRepository jogadorRepository,
                                    RegistroLesoesRepository lesoesRepository) {
        this.repository = repository;
        this.jogadorRepository = jogadorRepository;
        this.lesoesRepository = lesoesRepository;
    }

    public InscricaoAtleta registrarInscricaoPorNome(String atletaNome, String competicao) throws Exception {
        Objects.requireNonNull(atletaNome, "O nome do atleta não pode ser nulo.");
        Objects.requireNonNull(competicao, "O nome da competição não pode ser nulo.");

        Jogador jogador = jogadorRepository.findByNomeIgnoreCase(atletaNome)
                .stream()
                .findFirst()
                .orElseThrow(() -> new Exception("Jogador " + atletaNome + " não encontrado"));
        
        int idade = jogador.getIdade(); 
        boolean contratoAtivo = lesoesRepository.contratoAtivo(atletaNome); 

        return this.registrarInscricao(atletaNome, idade, contratoAtivo, competicao);
    }

    private InscricaoAtleta registrarInscricao(String atleta, int idade, boolean contratoAtivo, String competicao) {
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