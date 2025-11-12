package dev.com.protactic.infraestrutura.persistencia.jpa.fisico;

// Importa a interface de Resumo
import dev.com.protactic.aplicacao.principal.fisico.FisicoResumo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FisicoRepositorySpringData extends JpaRepository<FisicoJPA, Integer> {

    // Este método é usado pelo Domínio (para salvar e converter)
    List<FisicoJPA> findByJogadorId(Integer jogadorId);

    // --- (INÍCIO DA CORREÇÃO) ---
    // Este novo método é usado pela Aplicação (para consultas/GET)
    // O Spring Data vai automaticamente criar objetos que implementam FisicoResumo
    List<FisicoResumo> findAllByJogadorId(Integer jogadorId);
    // --- (FIM DA CORREÇÃO) ---
}