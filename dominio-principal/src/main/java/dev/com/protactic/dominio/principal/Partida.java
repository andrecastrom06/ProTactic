package dev.com.protactic.dominio.principal;

import java.util.Date;

public class Partida {
    private int id;
    private Clube clubeCasa;
    private Clube clubeVisitante;
    private Date dataJogo;
    private String hora;
    private int placarClubeCasa;
    private int placarClubeVisitante;

    public Partida(int id, Clube clubeCasa, Clube clubeVisitante, Date dataJogo,
                   String hora, int placarClubeCasa, int placarClubeVisitante) {
        this.id = id;
        this.clubeCasa = clubeCasa;
        this.clubeVisitante = clubeVisitante;
        this.dataJogo = dataJogo;
        this.hora = hora;
        this.placarClubeCasa = placarClubeCasa;
        this.placarClubeVisitante = placarClubeVisitante;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Clube getClubeCasa() { return clubeCasa; }
    public void setClubeCasa(Clube clubeCasa) { this.clubeCasa = clubeCasa; }

    public Clube getClubeVisitante() { return clubeVisitante; }
    public void setClubeVisitante(Clube clubeVisitante) { this.clubeVisitante = clubeVisitante; }

    public Date getDataJogo() { return dataJogo; }
    public void setDataJogo(Date dataJogo) { this.dataJogo = dataJogo; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public int getPlacarClubeCasa() { return placarClubeCasa; }
    public void setPlacarClubeCasa(int placarClubeCasa) { this.placarClubeCasa = placarClubeCasa; }

    public int getPlacarClubeVisitante() { return placarClubeVisitante; }
    public void setPlacarClubeVisitante(int placarClubeVisitante) { this.placarClubeVisitante = placarClubeVisitante; }

    public String getDescricao() {
        return clubeCasa.getNome() + " vs " + clubeVisitante.getNome();
    }
}