package com.haircutAPI.HaircutAPI.configuration;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import jakarta.annotation.PostConstruct;

/**
 * FirebaseInstallization
 */
@Configuration
public class FirebaseInstallization {

    @Bean
    public FirebaseMessaging installization() {
        try (FileInputStream serviceAccount = new FileInputStream("./firebase_key.json")) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp app = FirebaseApp.initializeApp(options);
            return FirebaseMessaging.getInstance(app);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return FirebaseMessaging.getInstance();
    }
}