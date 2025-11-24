package dev.com.protactic.apresentacao.principal.feature_06_inscricao_competicao;

import dev.com.protactic.aplicacao.principal.competicao.CompeticaoResumo;
import dev.com.protactic.aplicacao.principal.competicao.CompeticaoServicoAplicacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("backend/competicao")
@CrossOrigin(origins = "http://localhost:3000")
public class CompeticaoControlador {

    @Autowired
    private CompeticaoServicoAplicacao servicoAplicacao;

    @GetMapping(path = "pesquisa")
    public List<CompeticaoResumo> pesquisarResumos() {
        return servicoAplicacao.pesquisarResumos();
    }
}