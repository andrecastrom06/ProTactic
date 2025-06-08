package com.protactic.protactic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmpresarioController {
    @GetMapping("/empresario")
    public String empresario() {
        return "empresario";
    }
}