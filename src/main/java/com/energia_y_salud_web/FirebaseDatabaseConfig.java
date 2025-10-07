package com.energia_y_salud_web;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseDatabaseConfig {

    // 🔹 Permite configurar la URL desde application.properties
    @Value("${firebase.database.url}")
    private String firebaseUrl;

    // 🔹 Permite configurar el path al JSON (para Railway o local)
    @Value("${firebase.credentials.path:classpath:gimnasio-58f2c-firebase-adminsdk-fbsvc-72b58ecc5c.json}")
    private String credentialsPath;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            try (InputStream serviceAccount = new ClassPathResource(credentialsPath.replace("classpath:", "")).getInputStream()) {

                if (serviceAccount == null) {
                    throw new IllegalStateException("❌ Archivo de credenciales Firebase no encontrado en: " + credentialsPath);
                }

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl(firebaseUrl)
                        .build();

                FirebaseApp app = FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase inicializado correctamente con URL: " + firebaseUrl);
                return app;
            }
        } else {
            return FirebaseApp.getInstance();
        }
    }

    @Bean
    public DatabaseReference usuariosRef(FirebaseApp app) {
        return FirebaseDatabase.getInstance(app).getReference("usuarios");
    }
}
