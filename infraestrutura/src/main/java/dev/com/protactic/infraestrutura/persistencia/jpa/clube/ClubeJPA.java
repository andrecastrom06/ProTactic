package dev.com.protactic.infraestrutura.persistencia.jpa.clube;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column; // <-- IMPORTANTE
import java.util.List;

@Entity(name = "Clube")
@Table(name = "Clube") // 1. Mudei para "Clube" (maiúsculo) para bater com o seu SQL
public class ClubeJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    // --- INÍCIO DA CORREÇÃO ---
    // 2. Mapeia os campos Java (camelCase) para as colunas SQL (snake_case)
    @Column(name = "id_treinador")
    private Integer treinadorId;
    
    @Column(name = "id_analista")
    private Integer analistaId;
    
    @Column(name = "id_preparador")
    private Integer preparadorId;
    
    @Column(name = "id_competicao")
    private Integer competicaoId;
    
    @Column(name = "capitao") // No seu SQL, a coluna chama-se 'capitao'
    private Integer capitaoId; 
    // --- FIM DA CORREÇÃO ---

    private String nome;
    
    @Column(name = "cidade_estado")
    private String cidadeEstado;
    
    private String estadio;
    
    @ElementCollection
    @CollectionTable(name = "clube_jogador_ids", joinColumns = @JoinColumn(name = "clube_id"))
    @Column(name = "jogador_ids") // Nome da coluna na tabela de junção
    private List<Integer> jogadorIds;

    // Construtor vazio
    public ClubeJPA() {}

    // --- Getters e Setters ---
    // (Não vou repetir todos, mas certifique-se que os tem)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Integer getTreinadorId() { return treinadorId; }
    public void setTreinadorId(Integer treinadorId) { this.treinadorId = treinadorId; }
    public Integer getAnalistaId() { return analistaId; }
    public void setAnalistaId(Integer analistaId) { this.analistaId = analistaId; }
    public Integer getPreparadorId() { return preparadorId; }
    public void setPreparadorId(Integer preparadorId) { this.preparadorId = preparadorId; }
    public Integer getCompeticaoId() { return competicaoId; }
    public void setCompeticaoId(Integer competicaoId) { this.competicaoId = competicaoId; }
    public Integer getCapitaoId() { return capitaoId; }
    public void setCapitaoId(Integer capitaoId) { this.capitaoId = capitaoId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCidadeEstado() { return cidadeEstado; }
    public void setCidadeEstado(String cidadeEstado) { this.cidadeEstado = cidadeEstado; }
    public String getEstadio() { return estadio; }
    public void setEstadio(String estadio) { this.estadio = estadio; }
    public List<Integer> getJogadorIds() { return jogadorIds; }
    public void setJogadorIds(List<Integer> jogadorIds) { this.jogadorIds = jogadorIds; }
}