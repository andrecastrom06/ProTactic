package dev.com.protactic.infraestrutura.persistencia.jpa.registrocartao;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

// --- (INÍCIO DA CORREÇÃO) ---
@Entity(name = "RegistroCartao")
@Table(name = "registro_cartao") // 1. Corrigido para bater com o SQL
// --- (FIM DA CORREÇÃO) ---
public class RegistroCartaoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String atleta;
    private String tipo;

    // 2. Construtor vazio (JPA/ModelMapper)
    public RegistroCartaoJPA() {}

    // --- Getters e Setters ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getAtleta() { return atleta; }
    public void setAtleta(String atleta) { this.atleta = atleta; } // 3. Setter necessário

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; } // 4. Setter necessário
}