package com.energia_y_salud_web;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseDatabaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            try (InputStream serviceAccount = getClass().getClassLoader()
                    .getResourceAsStream("gimnasio-58f2c-firebase-adminsdk-fbsvc-72b58ecc5c.json")) {

                if (serviceAccount == null) {
                    throw new IllegalStateException("❌ Archivo de credenciales Firebase no encontrado en resources.");
                }

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("https://gimnasio-58f2c-default-rtdb.firebaseio.com")
                        .build();

                return FirebaseApp.initializeApp(options);
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

