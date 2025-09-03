package dev.protactic.sgb.principal.dominio;

public class Clube {
    private int id;
    private Treinador treinador;
    private Analista analista;
    private Preparador preparador;
    private Competicao competicao;
    private String nome;
    private String cidadeEstado;
    private String estadio;
    private Jogador capitao;
    private Jogador viceCapitao;

    public Clube(int id, Treinador treinador, Analista analista, Preparador preparador,
                 Competicao competicao, String nome, String cidadeEstado, String estadio,
                 Jogador capitao, Jogador viceCapitao) {
        this.id = id;
        this.treinador = treinador;
        this.analista = analista;
        this.preparador = preparador;
        this.competicao = competicao;
        this.nome = nome;
        this.cidadeEstado = cidadeEstado;
        this.estadio = estadio;
        this.capitao = capitao;
        this.viceCapitao = viceCapitao;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Treinador getTreinador() { return treinador; }
    public void setTreinador(Treinador treinador) { this.treinador = treinador; }

    public Analista getAnalista() { return analista; }
    public void setAnalista(Analista analista) { this.analista = analista; }

    public Preparador getPreparador() { return preparador; }
    public void setPreparador(Preparador preparador) { this.preparador = preparador; }

    public Competicao getCompeticao() { return competicao; }
    public void setCompeticao(Competicao competicao) { this.competicao = competicao; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCidadeEstado() { return cidadeEstado; }
    public void setCidadeEstado(String cidadeEstado) { this.cidadeEstado = cidadeEstado; }

    public String getEstadio() { return estadio; }
    public void setEstadio(String estadio) { this.estadio = estadio; }

    public Jogador getCapitao() { return capitao; }
    public void setCapitao(Jogador capitao) { this.capitao = capitao; }

    public Jogador getViceCapitao() { return viceCapitao; }
    public void setViceCapitao(Jogador viceCapitao) { this.viceCapitao = viceCapitao; }
}