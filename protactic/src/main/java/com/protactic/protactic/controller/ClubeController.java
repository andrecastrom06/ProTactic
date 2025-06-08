package com.protactic.protactic.controller;

import com.protactic.protactic.model.Clube;
import com.protactic.protactic.repository.ClubeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/clubes")
public class ClubeController {

    private final ClubeRepository clubeRepository;

    public ClubeController(ClubeRepository clubeRepository) {
        this.clubeRepository = clubeRepository;
    }

    @GetMapping
    public String listarClubes(Model model) {
        List<Clube> clubes = clubeRepository.findAll();
        model.addAttribute("clubes", clubes);
        return "clube-lista";
    }

    @GetMapping("/novo")
    public String novoClubeForm(Model model) {
        model.addAttribute("clube", new Clube());
        return "clube-form";
    }

    @PostMapping("/salvar")
    public String salvarClube(@ModelAttribute Clube clube) {
        clubeRepository.save(clube);
        return "redirect:/clubes";
    }

    @GetMapping("/editar/{cnpj}")
    public String editarClube(@PathVariable String cnpj, Model model) {
        Clube clube = clubeRepository.findById(cnpj).orElseThrow();
        model.addAttribute("clube", clube);
        return "clube-form";
    }

    @GetMapping("/deletar/{cnpj}")
    public String deletarClube(@PathVariable String cnpj) {
        clubeRepository.deleteById(cnpj);
        return "redirect:/clubes";
    }
}
