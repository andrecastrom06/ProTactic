package dev.com.protactic.apresentacao.principal.feature_07_definir_capitao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.com.protactic.aplicacao.principal.jogador.JogadorResumo;
import dev.com.protactic.aplicacao.principal.jogador.JogadorServicoAplicacao;

import dev.com.protactic.aplicacao.principal.clube.ClubeResumo;
import dev.com.protactic.aplicacao.principal.clube.ClubeServicoAplicacao;
import dev.com.protactic.dominio.principal.Capitao;

import dev.com.protactic.dominio.principal.capitao.CapitaoService;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("backend/capitao") 
public class DefinirCapitaoControlador { 

    private @Autowired JogadorServicoAplicacao jogadorServicoAplicacao;
    private @Autowired ClubeServicoAplicacao clubeServicoAplicacao;
    
    private @Autowired CapitaoService capitaoService; 
    
    private @Autowired CapitaoServiceProxy capitaoServiceProxy; 
    

    @GetMapping(path = "pesquisa-jogadores")
    public List<JogadorResumo> pesquisarResumosDeJogadores() {
        return jogadorServicoAplicacao.pesquisarResumos();
    }

    @GetMapping(path = "pesquisa-jogadores-por-clube/{clubeId}")
    public List<JogadorResumo> pesquisarResumosDeJogadoresPorClube(@PathVariable("clubeId") Integer clubeId) {
        return jogadorServicoAplicacao.pesquisarResumosPorClube(clubeId);
    }
    
    @GetMapping(path = "buscar-por-clube/{clubeId}")
    public Capitao buscarCapitaoDoClube(@PathVariable("clubeId") Integer clubeId) {
        return capitaoService.buscarCapitaoPorClube(clubeId); 
    }

    @GetMapping(path = "listar-todos")
    public List<ClubeResumo> listarTodosOsCapitaes() {
        return clubeServicoAplicacao.pesquisarResumos();
    }

    @PostMapping(path = "definir/{jogadorId}") 
    public void definirCapitao(@PathVariable("jogadorId") Integer jogadorId) {
        capitaoServiceProxy.definirCapitao(jogadorId);
    }
}