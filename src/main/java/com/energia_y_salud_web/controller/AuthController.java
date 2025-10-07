package com.energia_y_salud_web.controller;

import com.energia_y_salud_web.security.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        // Aquí validarías usuario contra la DB o Firebase
        if ("admin".equals(username) && "1234".equals(password)) {
            String token = jwtUtil.generarToken(username, "ADMIN");
            return Map.of("token", token);
        } else if ("user".equals(username) && "1234".equals(password)) {
            String token = jwtUtil.generarToken(username, "USER");
            return Map.of("token", token);
        } else {
            throw new RuntimeException("Credenciales inválidas");
        }
    }
}
