package dev.com.protactic.dominio.principal.premiacaoInterna;

import dev.com.protactic.dominio.principal.Jogador;
import dev.com.protactic.dominio.principal.Premiacao;
import java.util.List;
import java.util.Date;

public interface IPremiacaoRepository {
    Premiacao criarPremiacao(String nomePremiacao, Date dataPremiacao);
}
