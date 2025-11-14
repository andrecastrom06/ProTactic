package dev.com.protactic.aplicacao.principal.jogador;

// 1. Precisamos do LocalDate para a data do contrato
import java.time.LocalDate;

public interface JogadorResumo {
    
    // Campos que já tinhas:
    int getId();
    String getNome();
    String getPosicao();
    double getNota();
    int getIdade();
    Integer getClubeId(); 

    // --- (INÍCIO DAS CORREÇÕES) ---
    // Campos que faltavam para a tua página de Atletas
    
    String getStatus();         // Para o Status (Disponível/Indisponível)
    boolean isSaudavel();       // Para a Saúde (Saudável/Lesionado)
    int getGrauLesao();         // Para a Saúde (Grau da lesão)
    LocalDate getChegadaNoClube(); // Para a data do Contrato
    boolean isCapitao();        // Para a tag <FaStar />
    // --- (FIM DAS CORREÇÕES) ---
}