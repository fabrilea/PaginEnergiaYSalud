package com.energia_y_salud_web.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // ⚠️ En producción usá una clave más larga (mínimo 256 bits)
    private static final String SECRET_KEY = "clave_super_secreta_para_firmar_jwt_1234567890_!@#$%";
    private static final long EXPIRATION = 1000 * 60 * 60; // 1 hora

    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * Genera un token JWT con nombre de usuario (DNI) y rol.
     */
    public String generarToken(String username, String rol) {
        return Jwts.builder()
                .setSubject(username)
                .claim("rol", rol)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Devuelve los Claims (datos) embebidos en el token.
     */
    public Claims obtenerClaims(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Valida si el token es correcto y no ha expirado.
     */
    public boolean validarToken(String token) {
        try {
            Claims claims = obtenerClaims(token);
            Date expiracion = claims.getExpiration();
            return expiracion != null && expiracion.after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Obtiene el rol directamente del token (opcional, por conveniencia).
     */
    public String obtenerRol(String token) {
        try {
            return obtenerClaims(token).get("rol", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Obtiene el usuario (subject) directamente del token.
     */
    public String obtenerUsuario(String token) {
        try {
            return obtenerClaims(token).getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
