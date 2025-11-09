package dev.com.protactic.dominio.principal;

public class RegistroCartao {

    // 1. Removemos 'final' para o ModelMapper
    private String atleta;
    private String tipo;

    // 2. Construtor vazio obrigatório para o ModelMapper
    public RegistroCartao() {}

    // O seu construtor original (usado pelo Serviço)
    public RegistroCartao(String atleta, String tipo) {
        this.atleta = atleta;
        this.tipo = tipo;
    }

    public String getAtleta() {
        return atleta;
    }

    public String getTipo() {
        return tipo;
    }

    // 3. Setters obrigatórios para o ModelMapper
    public void setAtleta(String atleta) {
        this.atleta = atleta;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}