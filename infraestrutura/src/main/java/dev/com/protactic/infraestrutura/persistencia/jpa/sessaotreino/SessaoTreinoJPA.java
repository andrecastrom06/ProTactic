package dev.com.protactic.infraestrutura.persistencia.jpa.sessaotreino;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.JoinColumn;
import java.util.List;

@Entity(name = "SessaoTreino")
@Table(name = "SessaoTreino") 
public class SessaoTreinoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_clube") 
    private Integer clubeId;

    private String nome;

    @Column(name = "id_partida")
    private Integer partidaId;

    @ElementCollection
    @CollectionTable(name = "SessaoTreino_Convocados", joinColumns = @JoinColumn(name = "sessao_treino_id"))
    @Column(name = "jogador_id")
    private List<Integer> convocadosIds;

    public SessaoTreinoJPA() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Integer getPartidaId() { return partidaId; }
    public void setPartidaId(Integer partidaId) { this.partidaId = partidaId; }
    public List<Integer> getConvocadosIds() { return convocadosIds; }
    public void setConvocadosIds(List<Integer> convocadosIds) { this.convocadosIds = convocadosIds; }
    public Integer getClubeId() { return clubeId; }
    public void setClubeId(Integer clubeId) { this.clubeId = clubeId; }
}