package com.protactic.protactic.repository;

import com.protactic.protactic.model.Jogador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JogadorRepository extends JpaRepository<Jogador, String> {
}