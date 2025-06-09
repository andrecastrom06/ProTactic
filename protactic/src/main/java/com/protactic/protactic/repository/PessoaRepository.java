package com.protactic.protactic.repository;

import com.protactic.protactic.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, String> {}
