package dev.com.protactic.apresentacao.principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.cadastroAtleta.JogadorRepository;
import dev.com.protactic.dominio.principal.planejamentoCargaSemanal.PlanejamentoCargaSemanalService;

/**
 * Controlador de API REST para a História de Usuário #2:
 * Planejar carga semanal de treino.
 * * Este controlador expõe a lógica do PlanejamentoCargaSemanalService
 * para ser consumida pela interface (frontend).
 */
@RestController
@RequestMapping("backend/carga-semanal")
@CrossOrigin(origins = "http://localhost:3000")

public class PlanejamentoCargaSemanalControlador {

    // --- Injeção dos Serviços e Repositórios ---

    // 1. O Serviço de Domínio que contém a regra de negócio do BDD
    private @Autowired PlanejamentoCargaSemanalService planejamentoCargaSemanalService;

    // 2. O Repositório para buscar o objeto 'Jogador' completo
    private @Autowired JogadorRepository jogadorRepository;


    /**
     * Endpoint para POST /backend/carga-semanal/registrar/{jogadorId}
     * * Tenta registrar um treino para um jogador.
     * A lógica de negócio (contrato ativo, grau de lesão) é tratada
     * pelo serviço de domínio.
     */
    @PostMapping(path = "/registrar/{jogadorId}")
    public ResponseEntity<Void> registrarTreinamento(@PathVariable("jogadorId") Integer jogadorId) {
        
        // 1. Buscar a entidade de domínio completa
        Jogador jogador = jogadorRepository.buscarPorId(jogadorId);
        if (jogador == null) {
            // Se o jogador não existe, retorna 404 Not Found
            return ResponseEntity.notFound().build();
        }

        // 2. Chamar o Serviço de Domínio para executar a regra de negócio
        boolean sucesso = planejamentoCargaSemanalService.registrarTreino(jogador);

        // 3. Retornar a resposta HTTP correta
        if (sucesso) {
            // Sucesso! (Treino registado)
            return ResponseEntity.ok().build(); // Retorna 200 OK
        } else {
            // Falha! (Regra de negócio impediu, ex: jogador lesionado)
            return ResponseEntity.badRequest().build(); // Retorna 400 Bad Request
        }
    }
}