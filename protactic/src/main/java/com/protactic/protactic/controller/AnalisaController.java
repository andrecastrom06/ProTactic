package com.protactic.protactic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnalisaController {
    @GetMapping("/analisa")
    public String analisa() {
        return "analisa";
    }
}