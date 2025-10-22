package dev.com.protactic.dominio.principal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Clube {
    private int id;
    
    
    private Integer treinadorId;
    private Integer analistaId;
    private Integer preparadorId;
    private Integer competicaoId;

    private String nome;
    private String cidadeEstado;
    private String estadio;

    
    private Integer capitaoId;
    
    
    private List<Integer> jogadorIds;

    
    public Clube(int id, Integer treinadorId, Integer analistaId, Integer preparadorId,
                 Integer competicaoId, String nome, String cidadeEstado, String estadio,
                 Integer capitaoId) {
        this.id = id;
        this.treinadorId = treinadorId;
        this.analistaId = analistaId;
        this.preparadorId = preparadorId;
        this.competicaoId = competicaoId;
        this.nome = nome;
        this.cidadeEstado = cidadeEstado;
        this.estadio = estadio;
        this.capitaoId = capitaoId;
        this.jogadorIds = new ArrayList<>();
    }

    public Clube(String nome) {
        this.nome = nome;
        this.jogadorIds = new ArrayList<>();
    }
    
    // MUDANÇA: Método agora opera sobre IDs.
    // A lógica perigosa que modificava o 'jogador' (jogador.setClube(this)) foi REMOVIDA.
    // Isso deve ser feito pelo Application Service, que orquestra os dois agregados.
    public void adicionarJogadorId(Integer jogadorId) {
        if (jogadorId != null && !this.jogadorIds.contains(jogadorId)) {
            this.jogadorIds.add(jogadorId);
        }
        // REMOVIDO: if (jogador.getClube() != this) { jogador.setClube(this); }
    }
    
    // MUDANÇA: Método agora opera sobre IDs.
    public void removerJogadorId(Integer jogadorId) {
        if (jogadorId != null) {
            // Garante que está removendo o Objeto Integer, não o índice.
            this.jogadorIds.remove(jogadorId); 
        }
    }
    
    // MUDANÇA: O método 'possuiJogador(String nomeJogador)' foi RENOMEADO.
    // O Agregado Clube não deve saber o 'nome' de um Jogador, apenas seu ID.
    // A lógica de "buscar por nome" pertence a um repositório ou serviço.
    public boolean possuiJogadorId(Integer jogadorId) {
        return this.jogadorIds.stream()
                .anyMatch(id -> id.equals(jogadorId));
    }
    
    // MUDANÇA: Getter agora retorna a lista de IDs.
    public List<Integer> getJogadorIds() {
        return jogadorIds;
    }

    // MUDANÇA: Setter agora define a lista de IDs.
    public void setJogadorIds(List<Integer> jogadorIds) {
        this.jogadorIds = jogadorIds;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    // MUDANÇA: Getters e Setters para os IDs
    public Integer getTreinadorId() { return treinadorId; }
    public void setTreinadorId(Integer treinadorId) { this.treinadorId = treinadorId; }

    public Integer getAnalistaId() { return analistaId; }
    public void setAnalistaId(Integer analistaId) { this.analistaId = analistaId; }

    public Integer getPreparadorId() { return preparadorId; }
    public void setPreparadorId(Integer preparadorId) { this.preparadorId = preparadorId; }

    public Integer getCompeticaoId() { return competicaoId; }
    public void setCompeticaoId(Integer competicaoId) { this.competicaoId = competicaoId; }
    
    // MUDANÇA: Getters e Setters para os IDs
    public Integer getCapitaoId() { return capitaoId; }
    public void setCapitaoId(Integer capitaoId) { this.capitaoId = capitaoId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCidadeEstado() { return cidadeEstado; }
    public void setCidadeEstado(String cidadeEstado) { this.cidadeEstado = cidadeEstado; }

    public String getEstadio() { return estadio; }
    public void setEstadio(String estadio) { this.estadio = estadio; }

    // MUDANÇA: equals/hashCode agora são baseados no ID (correto para Entidades)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clube clube = (Clube) o;
        // Se os IDs não foram setados (novos objetos), não podem ser iguais
        if (this.id == 0 || clube.id == 0) return false;
        return id == clube.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}