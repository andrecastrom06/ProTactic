package dev.com.protactic.infraestrutura.persistencia.jpa.partida;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

@Entity(name = "Partida")
@Table(name = "Partida") // 1. O nome da tabela DEVE ser igual ao do seu SQL
public class PartidaJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // 2. Mapeia os IDs (o ModelMapper vai preencher)
    @Column(name = "id_clube_casa")
    private Integer clubeCasaId;

    @Column(name = "id_clube_visitante")
    private Integer clubeVisitanteId;

    @Column(name = "data_jogo")
    @Temporal(TemporalType.DATE)
    private Date dataJogo;

    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora; // Nota: O seu SQL usa 'TIME', o Java 'Date' pode guardar 'TIME'

    @Column(name = "placar_clube_casa")
    private int placarClubeCasa;

    @Column(name = "placar_clube_visitante")
    private int placarClubeVisitante;

    // Construtor vazio obrigatório para o JPA
    public PartidaJPA() {}

    // --- Getters e Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Integer getClubeCasaId() { return clubeCasaId; }
    public void setClubeCasaId(Integer clubeCasaId) { this.clubeCasaId = clubeCasaId; }
    public Integer getClubeVisitanteId() { return clubeVisitanteId; }
    public void setClubeVisitanteId(Integer clubeVisitanteId) { this.clubeVisitanteId = clubeVisitanteId; }
    public Date getDataJogo() { return dataJogo; }
    public void setDataJogo(Date dataJogo) { this.dataJogo = dataJogo; }
    public Date getHora() { return hora; }
    public void setHora(Date hora) { this.hora = hora; }
    public int getPlacarClubeCasa() { return placarClubeCasa; }
    public void setPlacarClubeCasa(int placarClubeCasa) { this.placarClubeCasa = placarClubeCasa; }
    public int getPlacarClubeVisitante() { return placarClubeVisitante; }
    public void setPlacarClubeVisitante(int placarClubeVisitante) { this.placarClubeVisitante = placarClubeVisitante; }
}