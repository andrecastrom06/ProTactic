package com.protactic.protactic.controller;

import com.protactic.protactic.model.Jogador;
import com.protactic.protactic.repository.JogadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class JogadorController {

    @Autowired
    private JogadorRepository jogadorRepository;

    @GetMapping("/jogador")
    public String jogador(Model model) {
        List<Jogador> jogadores = jogadorRepository.findAll();
        model.addAttribute("jogadores", jogadores);
        return "jogador";
    }
}
