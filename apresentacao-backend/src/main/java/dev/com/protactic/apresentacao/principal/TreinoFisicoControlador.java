package dev.com.protactic.apresentacao.principal;

import dev.com.protactic.aplicacao.principal.fisico.FisicoResumo;
import dev.com.protactic.aplicacao.principal.fisico.FisicoServicoAplicacao;
import dev.com.protactic.dominio.principal.Fisico;
import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.planejamentoCargaSemanal.PlanejamentoCargaSemanalService;
import dev.com.protactic.dominio.principal.planejamentoFisico.FisicoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional; 

@RestController
@RequestMapping("backend/treino-fisico")
@CrossOrigin(origins = "http://localhost:3000")

public class TreinoFisicoControlador {

    private @Autowired FisicoRepository fisicoRepository;

    private @Autowired FisicoServicoAplicacao fisicoServicoAplicacao;

    private @Autowired PlanejamentoCargaSemanalService planejamentoCargaSemanalService;

    private @Autowired JogadorRepository jogadorRepository;

    @GetMapping("/por-jogador/{jogadorId}")
    public List<FisicoResumo> buscarTreinosPorJogador(@PathVariable("jogadorId") Integer jogadorId) {
        return fisicoServicoAplicacao.pesquisarResumosPorJogador(jogadorId);
    }



    public record TreinoFisicoFormulario(
        String nome,
        String musculo,
        String intensidade,
        String descricao,
        Date dataInicio,
        Date dataFim
    ) {}

    @PostMapping("/salvar/{jogadorId}")
    public ResponseEntity<Fisico> salvarTreinoFisico(
            @PathVariable("jogadorId") Integer jogadorId,
            @RequestBody TreinoFisicoFormulario formulario) {

        Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
        if (jogador == null) {
            return ResponseEntity.notFound().build(); // Retorna 404
        }

    
        boolean aptoParaTreino = planejamentoCargaSemanalService.registrarTreino(jogador);

        if (!aptoParaTreino) {
            return ResponseEntity.badRequest().build(); 
        }

        Fisico novoTreino = new Fisico(
            0, 
            jogador,
            formulario.nome(),
            formulario.musculo(),
            formulario.intensidade(),
            formulario.descricao(),
            formulario.dataInicio(),
            formulario.dataFim(),
            "PLANEJADO" 
        );
       
        Fisico treinoSalvo = fisicoRepository.salvar(novoTreino);
        return ResponseEntity.ok(treinoSalvo);
    }
    
    @PutMapping("/editar/{treinoId}")
    public ResponseEntity<Fisico> editarTreinoFisico(
            @PathVariable("treinoId") Integer treinoId,
            @RequestBody TreinoFisicoFormulario formulario) {

        Optional<Fisico> treinoOpt = fisicoRepository.buscarPorId(treinoId);

        if (treinoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Fisico treinoExistente = treinoOpt.get();

        treinoExistente.setNome(formulario.nome());
        treinoExistente.setMusculo(formulario.musculo());
        treinoExistente.setIntensidade(formulario.intensidade());
        treinoExistente.setDescricao(formulario.descricao());
        treinoExistente.setDataInicio(formulario.dataInicio());
        treinoExistente.setDataFim(formulario.dataFim());

        Fisico treinoAtualizado = fisicoRepository.salvar(treinoExistente);

        return ResponseEntity.ok(treinoAtualizado);
    }

    public record StatusUpdateFormulario(
        String status
    ) {}

    @PatchMapping("/atualizar-status/{treinoId}")
    public ResponseEntity<Fisico> atualizarStatusTreino(
            @PathVariable("treinoId") Integer treinoId,
            @RequestBody StatusUpdateFormulario formulario) {

        Optional<Fisico> treinoOpt = fisicoRepository.buscarPorId(treinoId);

        if (treinoOpt.isEmpty()) {
            return ResponseEntity.notFound().build(); // Retorna 404
        }

        Fisico treinoExistente = treinoOpt.get();

        treinoExistente.setStatus(formulario.status());

        Fisico treinoAtualizado = fisicoRepository.salvar(treinoExistente);

        return ResponseEntity.ok(treinoAtualizado);
    }
    
    @PostMapping("/criar-protocolo-retorno/{jogadorId}")
    public ResponseEntity<Fisico> criarProtocoloDeRetorno(
            @PathVariable("jogadorId") Integer jogadorId,
            @RequestBody TreinoFisicoFormulario formulario) {

        Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
        if (jogador == null) {
            return ResponseEntity.notFound().build(); // Retorna 404
        }

        Fisico novoProtocolo = new Fisico(
            0, 
            jogador,
            formulario.nome(),
            formulario.musculo(),
            formulario.intensidade(),
            formulario.descricao(),
            formulario.dataInicio(),
            formulario.dataFim(),
            "PROTOCOLO_RETORNO" 
        );

        Fisico protocoloSalvo = fisicoRepository.salvar(novoProtocolo);

        return ResponseEntity.ok(protocoloSalvo);
    }
}