package com.energia_y_salud_web.controller;

import com.energia_y_salud_web.model.Usuario;
import com.energia_y_salud_web.security.JwtUtil;
import com.google.firebase.database.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final DatabaseReference usuariosRef;

    public AuthController(JwtUtil jwtUtil, DatabaseReference usuariosRef) {
        this.jwtUtil = jwtUtil;
        this.usuariosRef = usuariosRef;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials, HttpServletResponse response) {
        String dni = credentials.get("username");
        String password = credentials.get("password");

        if (dni == null || password == null || dni.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Campos vacíos"));
        }

        try {
            CompletableFuture<Usuario> future = new CompletableFuture<>();

            usuariosRef.child(dni).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        future.complete(null);
                        return;
                    }
                    Usuario user = snapshot.getValue(Usuario.class);
                    future.complete(user);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    future.completeExceptionally(error.toException());
                }
            });

            Usuario usuario = future.get();
            if (usuario == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Usuario no encontrado."));
            }

            if (!usuario.getPassword().equals(password)) {
                return ResponseEntity.status(401).body(Map.of("error", "Contraseña incorrecta."));
            }

            String rol = usuario.getRol() != null ? usuario.getRol() : "USER";
            String token = jwtUtil.generarToken(dni, rol);

            // 🍪 Cookie HttpOnly para validación de backend
            ResponseCookie cookie = ResponseCookie.from("JWT", token)
                    .httpOnly(true)
                    .secure(false) // ⚠️ poné true si usás HTTPS
                    .path("/")
                    .maxAge(3600)
                    .sameSite("Lax")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            // Enviamos token también al frontend
            return ResponseEntity.ok(Map.of("token", token));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Error de autenticación"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // 🍪 Borra cookie
        ResponseCookie cookie = ResponseCookie.from("JWT", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(Map.of("message", "Sesión cerrada"));
    }
}
