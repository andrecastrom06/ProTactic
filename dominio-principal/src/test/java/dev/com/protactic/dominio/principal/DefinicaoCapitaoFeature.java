package dev.com.protactic.dominio.principal;

import io.cucumber.java.Before;
import io.cucumber.java.pt.*;
import static org.junit.jupiter.api.Assertions.*;

import dev.com.protactic.dominio.principal.capitao.CapitaoService;
import dev.com.protactic.mocks.CapitaoMock;

import java.util.ArrayList;
import java.util.List;

public class DefinicaoCapitaoFeature {

    private Jogador jogador;
    private List<Jogador> jogadores;
    private CapitaoService service;
    private CapitaoMock repo;

    @Before // Executado antes de cada cenário para evitar interferência entre testes
    public void init() {
        repo = new CapitaoMock();
        repo.limpar(); 
        service = new CapitaoService(repo);
        jogadores = new ArrayList<>();
    }

    //Cenário com 1 jogador apto a ser capitão
    @Dado("um jogador chamado {string}")
    public void criarJogador(String nome) {
        jogador = new Jogador(nome);
        jogadores.clear();
        jogadores.add(jogador);
    }

    @E("ele possui contrato {string} com o {string}")
    public void setContrato(String status, String clubeNome) {
        Clube clube = new Clube(clubeNome);
        Contrato contrato = new Contrato(clube);
        contrato.setStatus("ativo".equalsIgnoreCase(status) ? "ATIVO" : "INATIVO");
        jogador.setClube(clube);
        jogador.setContrato(contrato);
    }

    @E("ele tem {string} de clube")
    public void setTempoClube(String tempo) {
        definirAnos(jogador, tempo);
    }

    @E("sua minutagem é {string}")
    public void setMinutagem(String minutagem) {
        jogador.setMinutagem(minutagem);
    }

    @Quando("o treinador tenta definir {string} como capitão")
    public void definirCapitaoUnico(String nome) {
        service.definirCapitao(jogador);
    }

    @Então("{string} deve ser definido como capitão do {string}")
    public void verificaCapitao(String nome, String clube) {
        Jogador c = repo.buscarCapitaoPorClube(clube);
        assertNotNull(c, "Capitão não foi salvo");
        assertTrue(c.isCapitao(), "Flag de capitão falsa");
        assertEquals(nome, c.getNome(), "Nome do capitão não bate");
    }

    @Então("{string} não deve ser definido como capitão do {string}")
    public void verificaNaoCapitao(String nome, String clube) {
        Jogador c = repo.buscarCapitaoPorClube(clube);
        if (c != null) {
            assertFalse(c.getNome().equals(nome) && c.isCapitao(),
                    "Jogador '" + nome + "' não deveria ser capitão");
        } else {
            assertNull(c);
        }
    }

    //Cenário com 2 jogadores para avliar desempate
    @Dado("dois jogadores {string} e {string}")
    public void criarDoisJogadores(String n1, String n2) {
        jogadores.clear();
        jogadores.add(new Jogador(n1));
        jogadores.add(new Jogador(n2));
    }

    @E("ambos possuem contrato {string} com o {string}")
    public void setContratoTodos(String status, String clube) {
        for (Jogador j : jogadores) {
            Clube c = new Clube(clube);
            Contrato contrato = new Contrato(c);
            contrato.setStatus("ativo".equalsIgnoreCase(status) ? "ATIVO" : "INATIVO");
            j.setClube(c);
            j.setContrato(contrato);
        }
    }

    @E("ambos têm minutagem {string}")
    public void setMinutagemTodos(String minutagem) {
        for (Jogador j : jogadores) {
            j.setMinutagem(minutagem);
        }
    }

    @E("{string} tem {string} de clube e {string} tem {string} de clube")
    public void setTempoClubeDiferente(String n1, String t1, String n2, String t2) {
        for (Jogador j : jogadores) {
            if (j.getNome().equals(n1)) definirAnos(j, t1);
            else if (j.getNome().equals(n2)) definirAnos(j, t2);
        }
    }

    @E("ambos têm {string} de clube")
    public void setTempoClubeIgual(String tempo) {
        for (Jogador j : jogadores) definirAnos(j, tempo);
    }

    //Empate total, treinador escolhe manualmente o capitão
    @Quando("o treinador tenta definir o capitão")
    public void definirCapitaoDois() {
        if (jogadores.size() == 2) {
            Jogador j1 = jogadores.get(0);
            Jogador j2 = jogadores.get(1);

            boolean j1Ok = service.podeSerCapitao(j1);
            boolean j2Ok = service.podeSerCapitao(j2);

            if (j1Ok && j2Ok) {
                if (j1.getAnosDeClube() > j2.getAnosDeClube()) service.definirCapitao(j1);
                else if (j2.getAnosDeClube() > j1.getAnosDeClube()) service.definirCapitao(j2);
                // empate total -> não define ninguém
            } else if (j1Ok) service.definirCapitao(j1);
            else if (j2Ok) service.definirCapitao(j2);
        } else if (jogadores.size() == 1) {
            service.definirCapitao(jogador);
        }
    }

    //Empate total, treinador escolhe manualmente o capitão
    @Então("o treinador deve escolher manualmente quem será o capitão do {string}")
    public void escolhaManual(String clube) {
        Jogador c = repo.buscarCapitaoPorClube(clube);
        assertNull(c, "Nenhum capitão deve ser definido em empate total");
    }

    //Desempate por tempo de clube
    @Então("{string} deve ser definido como capitão do {string} por ter mais tempo de clube")
    public void verificaCapitaoTempo(String nome, String clube) {
        Jogador c = repo.buscarCapitaoPorClube(clube);
        assertNotNull(c, "Capitão deveria ser definido");
        assertTrue(c.isCapitao(), "Flag de capitão falsa");
        assertEquals(nome, c.getNome(), "Capitão com mais tempo não definido corretamente");
    }

    //Método auxiliar para converter anos e meses para meses
    private void definirAnos(Jogador j, String tempo) {
        int meses = 0;
        tempo = tempo.trim().toLowerCase();

        if (tempo.contains("ano")) {
            try {
                String[] partes = tempo.split(" ");
                int anos = Integer.parseInt(partes[0]);
                meses += anos * 12;

                //verificação se também tem meses
                if (partes.length > 2 && partes[2].contains("mes")) {
                    int m = Integer.parseInt(partes[1]);
                    meses += m;
                }
            } catch (Exception e) {
                meses = 0;
            }
        } else if (tempo.contains("mes")) {
            try {
                meses = Integer.parseInt(tempo.split(" ")[0]);
            } catch (Exception e) {
                meses = 0;
            }
        }

        j.setAnosDeClube(meses); 
    }
}
