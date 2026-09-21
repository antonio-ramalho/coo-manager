package com.manager.coopafi.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Libera para todos os endpoints
                .allowedOrigins("http://localhost:4200") // Permite a porta do Angular
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Verbos permitidos
                .allowedHeaders("*") // Permite qualquer header
                .allowCredentials(true); // Necessário se for usar cookies/tokens no futuro
    }
}
