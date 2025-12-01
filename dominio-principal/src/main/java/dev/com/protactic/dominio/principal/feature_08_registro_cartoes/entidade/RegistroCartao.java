package dev.com.protactic.dominio.principal.feature_08_registro_cartoes.entidade;

public class RegistroCartao {

    private int id;
    private String atleta; 
    private String tipo;

  
    public RegistroCartao() {}

  
    public RegistroCartao(String atleta, String tipo) {
        this.atleta = atleta;
        this.tipo = tipo;
    }
    
   
    public RegistroCartao(int id, String atleta, String tipo) {
        this.id = id;
        this.atleta = atleta;
        this.tipo = tipo;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAtleta() {
        return atleta;
    }

    public void setAtleta(String atleta) {
        this.atleta = atleta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}