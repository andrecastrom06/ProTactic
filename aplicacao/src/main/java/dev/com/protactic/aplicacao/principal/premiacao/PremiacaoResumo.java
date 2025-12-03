package dev.com.protactic.aplicacao.principal.premiacao;

import org.springframework.beans.factory.annotation.Value; // Importante
import java.math.BigDecimal;
import java.util.Date;

public interface PremiacaoResumo {
    
    Integer getId();
    String getNome();
    Date getDataPremiacao();
    BigDecimal getValor();
    @Value("#{target.jogador.id}")
    Integer getJogadorId();
    @Value("#{target.jogador.nome}")
    String getJogadorNome();
}