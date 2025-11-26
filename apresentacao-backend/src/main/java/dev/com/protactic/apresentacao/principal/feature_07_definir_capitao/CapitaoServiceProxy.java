package dev.com.protactic.apresentacao.principal.feature_07_definir_capitao;

import dev.com.protactic.dominio.principal.capitao.CapitaoService;

import org.springframework.stereotype.Component;

@Component
public class CapitaoServiceProxy {
    
    private final CapitaoService realService;

    
    public CapitaoServiceProxy(CapitaoService capitaoService) {
        this.realService = capitaoService;
    }

    public void definirCapitao(Integer jogadorId) {
        try {
            realService.definirCapitaoPorId(jogadorId);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível definir o capitão. Verifique se o jogador existe e se pertence a um clube.", e);
        }
    }
}