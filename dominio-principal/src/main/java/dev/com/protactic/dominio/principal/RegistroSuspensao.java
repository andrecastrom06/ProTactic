package dev.com.protactic.dominio.principal;

public class RegistroSuspensao {
private final String atleta;
private final boolean suspenso;

public RegistroSuspensao(String atleta, boolean suspenso) {
    this.atleta = atleta;
    this.suspenso = suspenso;
}

public String getAtleta() {
    return atleta;
}

public boolean isSuspenso() {
    return suspenso;
}


}
