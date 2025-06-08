package com.protactic.protactic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClubeController {
    @GetMapping("/clube")
    public String clube() {
        return "clube";
    }
}