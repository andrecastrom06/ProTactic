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

/**
 * Controlador de API REST para a História "Planejar treino físico" (Mapa 1.1).
 * Este controlador é responsável por SALVAR e CONSULTAR os treinos físicos
 * (da tabela Fisico), após validá-los usando o "porteiro"
 * (PlanejamentoCargaSemanalService).
 */
@RestController
@RequestMapping("backend/treino-fisico")
public class TreinoFisicoControlador {

    // --- Injeção dos Serviços e Repositórios ---

    // 1. O Repositório de Domínio (para salvar o treino)
    private @Autowired FisicoRepository fisicoRepository;

    // 2. O Serviço de Aplicação (para ler os treinos)
    private @Autowired FisicoServicoAplicacao fisicoServicoAplicacao;

    // 3. O "PORTEIRO" (Serviço de validação da História #2)
    private @Autowired PlanejamentoCargaSemanalService planejamentoCargaSemanalService;

    // 4. O Repositório para buscar o Jogador
    private @Autowired JogadorRepository jogadorRepository;

    
    // --- Endpoints de CONSULTA (GET) ---

    /**
     * GET /backend/treino-fisico/por-jogador/{jogadorId}
     * Busca todos os treinos físicos já planejados para um jogador.
     */
    @GetMapping("/por-jogador/{jogadorId}")
    public List<FisicoResumo> buscarTreinosPorJogador(@PathVariable("jogadorId") Integer jogadorId) {
        return fisicoServicoAplicacao.pesquisarResumosPorJogador(jogadorId);
    }


    // --- Endpoints de COMANDO (POST) ---

    /**
     * DTO (Formulário) para receber os dados do treino físico.
     * Não precisamos do jogadorId aqui, pois ele virá na URL.
     */
    public record TreinoFisicoFormulario(
        String nome,
        String musculo,
        String intensidade,
        String descricao,
        Date dataInicio,
        Date dataFim
    ) {}

    /**
     * POST /backend/treino-fisico/salvar/{jogadorId}
     * Salva um novo plano de treino físico para um jogador.
     */
    @PostMapping("/salvar/{jogadorId}")
    public ResponseEntity<Fisico> salvarTreinoFisico(
            @PathVariable("jogadorId") Integer jogadorId,
            @RequestBody TreinoFisicoFormulario formulario) {

        // 1. Buscar a entidade de domínio 'Jogador'
        Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
        if (jogador == null) {
            return ResponseEntity.notFound().build(); // Retorna 404
        }

        // 2. USAR O "PORTEIRO" (O controlador que fizemos antes)
        // Verificamos se o jogador PODE ter o treino registrado
        boolean aptoParaTreino = planejamentoCargaSemanalService.registrarTreino(jogador);

        if (!aptoParaTreino) {
            // Regra de negócio falhou (lesionado ou sem contrato)
            return ResponseEntity.badRequest().build(); // Retorna 400
        }

        // 3. Se estiver apto, criar e salvar a entidade Fisico
        Fisico novoTreino = new Fisico(
            0, // O ID será gerado pelo banco
            jogador,
            formulario.nome(),
            formulario.musculo(),
            formulario.intensidade(),
            formulario.descricao(),
            formulario.dataInicio(),
            formulario.dataFim()
        );

        Fisico treinoSalvo = fisicoRepository.salvar(novoTreino);

        // 4. Retornar o objeto salvo
        return ResponseEntity.ok(treinoSalvo);
    }
    
    @PutMapping("/editar/{treinoId}")
    public ResponseEntity<Fisico> editarTreinoFisico(
            @PathVariable("treinoId") Integer treinoId,
            @RequestBody TreinoFisicoFormulario formulario) {

        // 1. Buscar o treino físico existente pelo seu ID
        Optional<Fisico> treinoOpt = fisicoRepository.buscarPorId(treinoId);

        if (treinoOpt.isEmpty()) {
            // Se o treino não existe, retorna 404 Not Found
            return ResponseEntity.notFound().build();
        }

        Fisico treinoExistente = treinoOpt.get();

        // 2. Atualizar todos os campos do treino existente com os dados do formulário
        // (Nota: O 'jogador' deste treino não pode ser alterado por esta rota)
        treinoExistente.setNome(formulario.nome());
        treinoExistente.setMusculo(formulario.musculo());
        treinoExistente.setIntensidade(formulario.intensidade());
        treinoExistente.setDescricao(formulario.descricao());
        treinoExistente.setDataInicio(formulario.dataInicio());
        treinoExistente.setDataFim(formulario.dataFim());

        // 3. Salvar as alterações no repositório
        // O JPA entende que, como 'treinoExistente' já tem um ID,
        // isto é uma atualização (UPDATE), e não uma criação (INSERT).
        Fisico treinoAtualizado = fisicoRepository.salvar(treinoExistente);

        // 4. Retornar o objeto atualizado
        return ResponseEntity.ok(treinoAtualizado);
    }
}