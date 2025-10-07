package com.energia_y_salud_web.controller;

import com.energia_y_salud_web.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UsuarioService usuarioService;

    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // 👉 HOME del panel admin
    @GetMapping
    public String adminHome(Model model) throws Exception {
        model.addAttribute("totalUsuarios", usuarioService.findAll().get().size());
        return "admin"; // templates/admin.html (tu panel principal)
    }


}
