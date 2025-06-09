package com.protactic.protactic.controller;

import com.protactic.protactic.model.Empresario;
import com.protactic.protactic.model.Pessoa;
import com.protactic.protactic.repository.EmpresarioRepository;
import com.protactic.protactic.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/empresario")
public class EmpresarioController {

    @Autowired
    private EmpresarioRepository empresarioRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("empresarios", empresarioRepository.findAll());
        return "empresario";
    }

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("empresario", new Empresario());
        model.addAttribute("pessoa", new Pessoa());
        return "empresario-form";
    }

  @PostMapping("/salvar")
public String salvar(@ModelAttribute Pessoa pessoa, @ModelAttribute Empresario empresario) {
    Pessoa pessoaSalva = pessoaRepository.save(pessoa); // Salva e obtém o CPF

    empresario.setCpf(pessoaSalva.getCpf());            // Usa o CPF retornado da pessoa salva
    empresario.setPessoa(pessoaSalva);                  // Faz o vínculo corretamente

    empresarioRepository.save(empresario);              // Agora pode salvar o Empresario

    return "redirect:/empresario";
}




    @GetMapping("/editar/{cpf}")
    public String editar(@PathVariable String cpf, Model model) {
        Empresario empresario = empresarioRepository.findById(cpf).orElseThrow();
        model.addAttribute("empresario", empresario);
        model.addAttribute("pessoa", empresario.getPessoa());
        return "empresario-form";
    }

    @GetMapping("/excluir/{cpf}")
    public String excluir(@PathVariable String cpf) {
        empresarioRepository.deleteById(cpf);
        pessoaRepository.deleteById(cpf);
        return "redirect:/empresario";
    }
}
