package dev.com.protactic.apresentacao.principal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Importa os SERVIÇOS DE APLICAÇÃO (Queries)
import dev.com.protactic.aplicacao.principal.contrato.ContratoResumo;
import dev.com.protactic.aplicacao.principal.contrato.ContratoServicoAplicacao;

// Importa os SERVIÇOS DE DOMÍNIO (Comandos) e Repositórios necessários
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.dispensa.DispensaService;

@RestController
@RequestMapping("backend/contrato")
public class DispensaJogadorControlador {

    // --- Injeção dos Serviços ---

    private @Autowired ContratoServicoAplicacao contratoServicoAplicacao;
    private @Autowired DispensaService dispensaService;
    private @Autowired JogadorRepository jogadorRepository;
    

    // --- Endpoints de CONSULTA (GET) ---
    // (Esta parte está correta e não muda)

    @GetMapping(path = "pesquisa")
    public List<ContratoResumo> pesquisarResumos() {
        return contratoServicoAplicacao.pesquisarResumos();
    }

    @GetMapping(path = "pesquisa-por-clube/{clubeId}")
    public List<ContratoResumo> pesquisarResumosPorClube(@PathVariable("clubeId") Integer clubeId) {
        return contratoServicoAplicacao.pesquisarResumosPorClube(clubeId);
    }

    
    // --- Endpoints de COMANDO (POST) ---

  
    @PostMapping(path = "/dispensar/{jogadorId}")
    public void dispensarJogador(@PathVariable("jogadorId") Integer jogadorId) {
        
        // 1. Buscar a entidade de domínio completa
        Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
        if (jogador == null) {
            throw new RuntimeException("Jogador não encontrado: " + jogadorId);
        }

        // --- (INÍCIO DA CORREÇÃO) ---
        // 2. O método 'dispensarJogador' pode lançar uma Exceção Verificada.
        //    Precisamos de a "apanhar" (catch) e "re-lançar" (throw)
        //    como uma Exceção Não-Verificada (RuntimeException),
        //    que o Spring sabe como tratar (ele irá retornar um erro 500).
        try {
            dispensaService.dispensarJogador(jogador);
        } catch (Exception e) {
            // Re-lança a exceção original como uma RuntimeException
            throw new RuntimeException("Erro ao tentar dispensar o jogador: " + e.getMessage(), e);
        }
        // --- (FIM DA CORREÇÃO) ---
    }
} 