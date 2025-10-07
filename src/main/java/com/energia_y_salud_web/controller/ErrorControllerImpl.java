package com.energia_y_salud_web.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorControllerImpl implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object statusObj = request.getAttribute("jakarta.servlet.error.status_code");
        int status = statusObj != null ? (Integer) statusObj : 0;

        switch (status) {
            case 403 -> {
                model.addAttribute("mensajeError", "❌ No tienes permiso para acceder a esta página.");
                return "error-usuario";
            }
            case 404 -> {
                model.addAttribute("mensajeError", "⚠️ Página no encontrada (404).");
                return "error-usuario";
            }
            case 500 -> {
                model.addAttribute("mensajeError", "💥 Error interno del servidor (500).");
                return "error-usuario";
            }
            default -> {
                model.addAttribute("mensajeError", "Ha ocurrido un error inesperado.");
                return "error-usuario";
            }
        }
    }
}
