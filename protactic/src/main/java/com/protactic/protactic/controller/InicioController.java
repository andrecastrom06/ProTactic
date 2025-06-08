package com.protactic.protactic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    @GetMapping("/")
    public String home() {
        return "inicio"; // vai procurar templates/inicio.html
    }
}
