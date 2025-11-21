package dev.com.protactic.apresentacao.principal;

import dev.com.protactic.aplicacao.principal.clube.ClubeResumo;
import dev.com.protactic.aplicacao.principal.clube.ClubeServicoAplicacao;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("backend/clube") 
@CrossOrigin(origins = "http://localhost:3000") 
public class ClubeControlador {

    private final ClubeServicoAplicacao clubeServico;

    public ClubeControlador(ClubeServicoAplicacao clubeServico) {
        this.clubeServico = clubeServico;
    }

    @GetMapping("/pesquisa")
    public List<ClubeResumo> pesquisarClubes() {
        return clubeServico.pesquisarResumos();
    }

    @GetMapping("/pesquisa/competicao")
    public List<ClubeResumo> pesquisarClubesPorCompeticao(@RequestParam Integer competicaoId) {
        return clubeServico.pesquisarResumosPorCompeticao(competicaoId);
    }
}