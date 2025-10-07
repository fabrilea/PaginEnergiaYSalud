package com.energia_y_salud_web.config;

import com.energia_y_salud_web.security.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 🔒 Desactivar CSRF (usamos JWT)
            .csrf(csrf -> csrf.disable())

            // 🔓 Definir rutas públicas
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/error", "/error/**").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

                // 🔐 Rutas protegidas
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/usuario/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )

            // ⚙️ Política sin sesión
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // ⚙️ Manejo de errores personalizado
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationEntryPoint()) // sin token → 401
                .accessDeniedHandler(accessDeniedHandler())           // sin permisos → JSON 403
            )

            // 🔎 Filtro JWT antes del UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ❌ Evitar redirección a /error → responder JSON limpio
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"Acceso denegado: se requiere rol ADMIN\"}");
        };
    }

    // 🔐 Cuando no hay token → 401 Unauthorized
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"No autorizado: inicia sesión para continuar\"}");
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
