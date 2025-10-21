package dev.com.protactic.dominio.principal;

public class RegistroCartao {
private final String atleta;
private final String tipo;

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

}
