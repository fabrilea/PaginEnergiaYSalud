package com.energia_y_salud_web.controller;

import com.energia_y_salud_web.model.Usuario;
import com.energia_y_salud_web.security.JwtUtil;
import com.google.firebase.database.*;
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
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
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

            Usuario usuario = future.get(); // Espera la respuesta Firebase

            if (usuario == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Usuario no encontrado."));
            }

            // Validar contraseña (en texto plano, o BCrypt si la tenés cifrada)
            if (!usuario.getPassword().equals(password)) {
                return ResponseEntity.status(401).body(Map.of("error", "Contraseña incorrecta."));
            }

            String rol = usuario.getRol() != null ? usuario.getRol() : "USER";
            String token = jwtUtil.generarToken(dni, rol);

            return ResponseEntity.ok(Map.of("token", token));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Error de autenticación"));
        }
    }
}
