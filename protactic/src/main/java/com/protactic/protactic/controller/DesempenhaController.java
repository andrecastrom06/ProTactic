package com.protactic.protactic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DesempenhaController {
    @GetMapping("/desempenha")
    public String desempenha() {
        return "desempenha";
    }  
}