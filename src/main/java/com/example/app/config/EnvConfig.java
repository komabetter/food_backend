package com.example.app.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {
    
    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure()
                .directory("./") // Look for .env in the current directory
                .ignoreIfMissing()
                .load();
    }
}