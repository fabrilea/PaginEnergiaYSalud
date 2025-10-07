package com.energia_y_salud_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login"; // Renderiza templates/login.html
    }

    @GetMapping("/")
    public String redirigirAlLogin() {
        return "redirect:/login";
    }
}
