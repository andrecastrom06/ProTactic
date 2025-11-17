package dev.com.protactic.infraestrutura.persistencia.jpa.escalacao;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity(name = "Escalacao")
@Table(name = "Escalacao")
public class EscalacaoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "id_partida")
    private Integer partidaId;

    private String esquema;

    @Column(name = "id_jogador1")
    private Integer idJogador1;
    @Column(name = "id_jogador2")
    private Integer idJogador2;
    @Column(name = "id_jogador3")
    private Integer idJogador3;
    @Column(name = "id_jogador4")
    private Integer idJogador4;
    @Column(name = "id_jogador5")
    private Integer idJogador5;
    @Column(name = "id_jogador6")
    private Integer idJogador6;
    @Column(name = "id_jogador7")
    private Integer idJogador7;
    @Column(name = "id_jogador8")
    private Integer idJogador8;
    @Column(name = "id_jogador9")
    private Integer idJogador9;
    @Column(name = "id_jogador10")
    private Integer idJogador10;
    @Column(name = "id_jogador11")
    private Integer idJogador11;

    public EscalacaoJPA() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Integer getPartidaId() { return partidaId; }
    public void setPartidaId(Integer partidaId) { this.partidaId = partidaId; }
    public String getEsquema() { return esquema; }
    public void setEsquema(String esquema) { this.esquema = esquema; }
    public Integer getIdJogador1() { return idJogador1; }
    public void setIdJogador1(Integer idJogador1) { this.idJogador1 = idJogador1; }
    public Integer getIdJogador2() { return idJogador2; }
    public void setIdJogador2(Integer idJogador2) { this.idJogador2 = idJogador2; }
    public Integer getIdJogador3() { return idJogador3; }
    public void setIdJogador3(Integer idJogador3) { this.idJogador3 = idJogador3; }
    public Integer getIdJogador4() { return idJogador4; }
    public void setIdJogador4(Integer idJogador4) { this.idJogador4 = idJogador4; }
    public Integer getIdJogador5() { return idJogador5; }
    public void setIdJogador5(Integer idJogador5) { this.idJogador5 = idJogador5; }
    public Integer getIdJogador6() { return idJogador6; }
    public void setIdJogador6(Integer idJogador6) { this.idJogador6 = idJogador6; }
    public Integer getIdJogador7() { return idJogador7; }
    public void setIdJogador7(Integer idJogador7) { this.idJogador7 = idJogador7; }
    public Integer getIdJogador8() { return idJogador8; }
    public void setIdJogador8(Integer idJogador8) { this.idJogador8 = idJogador8; }
    public Integer getIdJogador9() { return idJogador9; }
    public void setIdJogador9(Integer idJogador9) { this.idJogador9 = idJogador9; }
    public Integer getIdJogador10() { return idJogador10; }
    public void setIdJogador10(Integer idJogador10) { this.idJogador10 = idJogador10; }
    public Integer getIdJogador11() { return idJogador11; }
    public void setIdJogador11(Integer idJogador11) { this.idJogador11 = idJogador11; }
}