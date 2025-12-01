package dev.com.protactic.dominio.principal;

import io.cucumber.java.pt.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Clube;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Jogador;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;
import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.entidade.Partida;
import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.repositorio.PartidaRepository;
import dev.com.protactic.dominio.principal.feature_10_treino_tatico.entidade.SessaoTreino;
import dev.com.protactic.dominio.principal.feature_10_treino_tatico.repositorio.SessaoTreinoRepository;
import dev.com.protactic.dominio.principal.feature_10_treino_tatico.servico.SessaoTreinoService;
import dev.com.protactic.mocks.SessaoTreinoMock;
import dev.com.protactic.mocks.PartidaMock;
import dev.com.protactic.mocks.JogadorMock;

import io.cucumber.java.Before;

public class SessoesTreinoTaticoFeature {

    private SessaoTreinoRepository sessaoRepo;
    private PartidaRepository partidaRepo; 
    private JogadorRepository jogadorRepo; 
    private SessaoTreinoService service;

    private Partida partidaDoTeste;
    private List<Jogador> jogadoresDoTeste;
    private SessaoTreino sessaoResultado;
    private Exception excecaoOcorrida;
    private final int CLUBE_ID_TESTE = 1;

    @Before
    public void setup() {
        sessaoRepo = new SessaoTreinoMock();
        partidaRepo = new PartidaMock(); 
        jogadorRepo = new JogadorMock(); 

        service = new SessaoTreinoService(sessaoRepo, partidaRepo, jogadorRepo);

        partidaDoTeste = null;
        jogadoresDoTeste = new ArrayList<>();
        sessaoResultado = null;
        excecaoOcorrida = null;
    }

    @Dado("que existe o jogo {string} no calendário")
    public void que_existe_um_jogo_no_calendario(String nomePartida) {
        String[] nomes = nomePartida.split(" vs ");
        if (nomes.length < 2) nomes = new String[] { nomes[0], "Adversário Desconhecido" };

        Clube clubeCasa = new Clube(nomes[0].trim());
        Clube clubeVisitante = new Clube(nomes[1].trim());
        
        partidaDoTeste = new Partida(); 
        partidaDoTeste.setClubeCasa(clubeCasa);
        partidaDoTeste.setClubeVisitante(clubeVisitante);
        partidaDoTeste.setDataJogo(new Date()); 
        
        ((PartidaMock) partidaRepo).salvar(partidaDoTeste); 
    }

    @Dado("os jogadores {string} e {string} estão disponíveis")
    public void os_jogadores_e_estao_disponiveis(String nome1, String nome2) {
        Jogador j1 = new Jogador(nome1);
        j1.setStatus("Disponível"); 
        jogadorRepo.salvar(j1);
        
        Jogador j2 = new Jogador(nome2);
        j2.setStatus("Disponível"); 
        jogadorRepo.salvar(j2);
        
        jogadoresDoTeste.add(j1);
        jogadoresDoTeste.add(j2);
    }
    
    @Dado("que o jogador {string} tem o status {string}")
    public void que_o_jogador_tem_o_status(String nomeJogador, String status) {
        Jogador j = new Jogador(nomeJogador);
        j.setStatus(status);
        jogadorRepo.salvar(j);
        jogadoresDoTeste.add(j);
    }
    
    @Dado("que já existe a sessão de treino {string} para o jogo {string}")
    public void que_ja_existe_a_sessao_de_treino(String nomeSessao, String nomeJogo) {
        if (partidaDoTeste == null || !partidaDoTeste.getDescricao().equals(nomeJogo)) {
             que_existe_um_jogo_no_calendario(nomeJogo);
        }
        service.criarSessao(nomeSessao, partidaDoTeste, new ArrayList<>(), CLUBE_ID_TESTE);
    }
    
    @Dado("que não existem jogos no calendário")
    public void que_nao_existem_jogos_no_calendario() {
        partidaDoTeste = null;
    }
    
    @Quando("o treinador cria a sessão de treino {string} para o jogo {string}")
    public void o_treinador_cria_a_sessao_de_treino_para_o_jogo(String nomeSessao, String nomeJogo) {
        if (partidaDoTeste == null || !partidaDoTeste.getDescricao().equals(nomeJogo)) {
             fail("O @Dado de setup da partida não foi chamado ou a partida está errada.");
        }
        
        List<Integer> idsJogadores = jogadoresDoTeste.stream()
                                                    .map(Jogador::getId)
                                                    .collect(Collectors.toList());
        
        try {
            sessaoResultado = service.criarSessaoPorIds(
                nomeSessao,
                partidaDoTeste.getId(),
                idsJogadores,
                CLUBE_ID_TESTE
            );
        } catch (Exception e) {
            this.excecaoOcorrida = e;
        }
    }

    @Quando("o treinador cria uma sessão de treino para o jogo {string}")
    public void o_treinador_cria_uma_sessao_de_treino_para_o_jogo(String nomeJogo) {
        List<Integer> idsJogadores = jogadoresDoTeste.stream()
                .filter(Jogador::isDisponivel) 
                .map(Jogador::getId)
                .collect(Collectors.toList());
        
        try {
            sessaoResultado = service.criarSessaoPorIds(
                "Sessao para " + nomeJogo, 
                partidaDoTeste.getId(), 
                idsJogadores,
                CLUBE_ID_TESTE
            );
        } catch (Exception e) {
            this.excecaoOcorrida = e;
        }
    }

    @Quando("o treinador tenta criar uma sessão de treino tático")
    public void o_treinador_tenta_criar_uma_sessao_de_treino_tatico() {
        try {
            Integer partidaId = (partidaDoTeste != null) ? partidaDoTeste.getId() : null;
            sessaoResultado = service.criarSessaoPorIds("Treino Tatico Teste", partidaId, new ArrayList<>(), CLUBE_ID_TESTE);
        } catch (Exception e) {
            this.excecaoOcorrida = e;
        }
    }


    @Então("a sessão de treino {string} deve ser registrada")
    public void a_sessao_de_treino_deve_ser_registrada(String nomeSessao) {
        assertNull(excecaoOcorrida, "Não deveria ter ocorrido exceção");
        assertNotNull(sessaoResultado, "A sessão não foi criada");
        assertEquals(nomeSessao, sessaoResultado.getNome());
    }
    
    @Então("ela deve estar vinculada à partida contra o {string}")
    public void ela_deve_estar_vinculada_a_partida_contra_o(String adversario) {
        assertNotNull(sessaoResultado.getPartida(), "Sessão não foi vinculada a nenhuma partida");
        assertEquals(adversario, sessaoResultado.getPartida().getClubeVisitante().getNome());
    }
    
    @Então("a sessão de treino {string} deve estar vinculada ao jogo {string}")
    public void a_sessao_de_treino_deve_estar_vinculada_ao_jogo(String nomeSessao, String nomeJogo) {
        assertNull(excecaoOcorrida, "Não deveria ter ocorrido exceção");
        List<SessaoTreino> sessoes = sessaoRepo.listarPorPartida(nomeJogo);
        boolean encontrada = sessoes.stream()
            .anyMatch(s -> s.getNome().equals(nomeSessao) && s.getPartida().getDescricao().equals(nomeJogo));
        assertTrue(encontrada, "A sessão " + nomeSessao + " não foi encontrada ou vinculada ao jogo " + nomeJogo);
    }
    
    @Então("o sistema deve permitir a criação da sessão")
    public void o_sistema_deve_permitir_a_criacao_da_sessao() {
         assertNull(excecaoOcorrida, "Não deveria ter ocorrido exceção");
         assertNotNull(sessaoResultado, "A sessão deveria ter sido criada");
    }

    @Então("o jogador {string} deve aparecer na lista de convocação")
    public void o_jogador_deve_aparecer_na_lista_de_convocacao(String nomeJogador) {
        boolean encontrado = sessaoResultado.getConvocados().stream()
                .anyMatch(j -> j.getNome().equals(nomeJogador));
        assertTrue(encontrado, "Jogador " + nomeJogador + " não encontrado na lista de convocados");
    }
    
    @Então("o jogador {string} não deve aparecer na lista de convocação")
    public void o_jogador_nao_deve_aparecer_na_lista_de_convocacao(String nomeJogador) {
         boolean encontrado = sessaoResultado.getConvocados().stream()
                .anyMatch(j -> j.getNome().equals(nomeJogador));
        assertFalse(encontrado, "Jogador " + nomeJogador + " foi convocado indevidamente");
    }
    
    @Então("o sistema deve impedir a criação da sessão")
    public void o_sistema_deve_impedir_a_criacao_da_sessao() {
        assertNotNull(excecaoOcorrida, "Uma exceção era esperada");
    }

    @Então("o sistema deve exibir a mensagem {string}")
    public void o_sistema_deve_exibir_a_mensagem(String mensagem) {
        assertNotNull(excecaoOcorrida, "Uma exceção era esperada");
        assertEquals(mensagem, excecaoOcorrida.getMessage());
    }
}