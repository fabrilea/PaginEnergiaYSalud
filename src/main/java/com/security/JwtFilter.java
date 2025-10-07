package com.energia_y_salud_web.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // ⛔ Ignorar recursos estáticos y rutas públicas
        if (path.startsWith("/css") || path.startsWith("/js") ||
            path.startsWith("/images") || path.startsWith("/favicon") ||
            path.startsWith("/auth") || path.equals("/login") ||
            path.equals("/") || path.startsWith("/error")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = null;

        // 1️⃣ Intentar obtener desde header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        // 2️⃣ Si no hay header, intentar obtener desde cookie
        if (token == null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JWT".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null) {
            try {
                if (jwtUtil.validarToken(token)) {
                    Claims claims = jwtUtil.obtenerClaims(token);
                    String username = claims.getSubject();
                    String rol = claims.get("rol", String.class);

                    logger.info("🔐 [{}] Token válido → usuario={}, rol={}, ruta={}",
                            request.getMethod(), username, rol, path);

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    Collections.singleton(() -> "ROLE_" + rol)
                            );
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);

                } else {
                    logger.warn("⚠️ Token inválido o expirado → {}", path);
                }
            } catch (Exception e) {
                logger.error("❌ Error al procesar token en {} → {}", path, e.getMessage());
            }
        } else {
            logger.debug("🔸 Sin token JWT en {} {}", request.getMethod(), path);
        }

        filterChain.doFilter(request, response);
    }
}
