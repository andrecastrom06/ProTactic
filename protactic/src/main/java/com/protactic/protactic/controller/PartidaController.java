package com.protactic.protactic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PartidaController {
    @GetMapping("/partida")
    public String partida() {
        return "partida";
    }
}