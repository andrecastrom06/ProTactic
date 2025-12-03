package dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.entidade;

import java.util.Date;

import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.entidade.Clube;

public class Partida {
    private int id;
    private Clube clubeCasa;
    private Clube clubeVisitante;
    private Date dataJogo;
    private String hora;
    private int placarClubeCasa;
    private int placarClubeVisitante;
    
    public Partida() {}

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
    public enum Resultado {
        VITORIA, DERROTA, EMPATE, NAO_REALIZADA
    }
    public Resultado obterResultadoPara(Integer clubeId) {
        if (placarClubeCasa == -1 || placarClubeVisitante == -1) { 
            return Resultado.NAO_REALIZADA; 
        }

        boolean souMandante = this.clubeCasa.getId() == clubeId; 
        boolean souVisitante = this.clubeVisitante.getId() == clubeId;

        if (!souMandante && !souVisitante) {
            throw new IllegalArgumentException("O clube informado não participou desta partida.");
        }

        if (placarClubeCasa == placarClubeVisitante) {
            return Resultado.EMPATE;
        }

        if (souMandante) {
            return placarClubeCasa > placarClubeVisitante ? Resultado.VITORIA : Resultado.DERROTA;
        } else {
            return placarClubeVisitante > placarClubeCasa ? Resultado.VITORIA : Resultado.DERROTA;
        }
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