package com.energia_y_salud_web;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;

@SpringBootApplication
public class GimnasioWebApp {
    public static void main(String[] args) throws Exception {
        // Leer JSON desde variable de entorno
        String firebaseConfig = System.getenv("FIREBASE_CONFIG");
        if (firebaseConfig == null || firebaseConfig.isBlank()) {
            throw new IllegalStateException("FIREBASE_CONFIG no está definido en las variables de entorno");
        }

        // Escribir config en un archivo temporal
        File tempFile = File.createTempFile("firebase", ".json");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(firebaseConfig);
        }

        // Inicializar Firebase con el archivo temporal
        try (FileInputStream serviceAccount = new FileInputStream(tempFile)) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://gimnasio-58f2c-default-rtdb.firebaseio.com")
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        }

        // Levantar Spring Boot
        SpringApplication.run(GimnasioWebApp.class, args);
    }
}
