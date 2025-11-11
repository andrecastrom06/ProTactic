package dev.com.protactic.dominio.principal;

/**
 * Classe de DOMÍNIO.
 * Esta versão inclui o 'id' para corrigir o erro de mapeamento 
 * vindo do banco (visto na imagem ...f8483e.png).
 */
public class RegistroCartao {

    // 1. Adicionamos o 'id'
    private int id;
    private String atleta; 
    private String tipo;

    /**
     * 2. Construtor vazio (ótimo para JPA, ModelMapper, etc.)
     * Como você já tinha feito.
     */
    public RegistroCartao() {}

    /**
     * 3. Construtor usado pelo SERVIÇO para criar um NOVO cartão
     * (O seu construtor original).
     */
    public RegistroCartao(String atleta, String tipo) {
        this.atleta = atleta;
        this.tipo = tipo;
    }
    
    /**
     * 4. (O MAIS IMPORTANTE) Construtor que corrige o erro da imagem.
     * Usado pelo REPOSITÓRIO para mapear dados vindos do banco.
     */
    public RegistroCartao(int id, String atleta, String tipo) {
        this.id = id;
        this.atleta = atleta;
        this.tipo = tipo;
    }

    // --- Getters e Setters para TODOS os campos ---

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