package com.energia_y_salud_web.config;

import com.energia_y_salud_web.security.JwtFilter;
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
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            // 🟢 Públicos
            .requestMatchers("/", "/login", "/error", "/error/**", "/auth/**").permitAll()
            .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
        
            // 🟢 Vistas Thymeleaf (el control real lo hace auth.js)
            .requestMatchers("/admin", "/admin/**", "/usuario", "/usuario/**").permitAll()
        
            // 🔒 API REST (solo si las tenés separadas)
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .requestMatchers("/api/usuario/**").hasAnyRole("USER", "ADMIN")
        
            .anyRequest().authenticated()
        )
        
        

            // 🚫 JWT → sin sesión
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // ⚠️ Manejo de errores visual con Thymeleaf
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationEntryPoint()) // sin token → login
                .accessDeniedHandler(accessDeniedHandler())           // sin permiso → error
            )

            // 🔒 Agregar filtro JWT antes del auth estándar
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 🔸 403 → redirigir a /error (usa tu ErrorControllerImpl)
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        AccessDeniedHandlerImpl handler = new AccessDeniedHandlerImpl();
        handler.setErrorPage("/error");
        return handler;
    }

    // 🔸 401 → redirigir al login (solo si intenta acceder sin token)
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> response.sendRedirect("/login");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
