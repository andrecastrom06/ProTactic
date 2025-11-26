package dev.com.protactic.apresentacao.principal.feature_08_registro_cartoes;

import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.RegistroCartoesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class LimparCartoesComando implements ComandoRegistroCartao {

    private final RegistroCartoesService registroCartoesService;
    private final String atletaNome;

    public LimparCartoesComando(RegistroCartoesService registroCartoesService, String atletaNome) {
        this.registroCartoesService = registroCartoesService;
        this.atletaNome = atletaNome;
    }

    @Override
    public ResponseEntity<?> executar() {
        try {
            registroCartoesService.limparCartoes(atletaNome);
            
            return ResponseEntity.ok().build(); 
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao limpar cartões: " + e.getMessage());
        }
    }
}