package com.dn.projectdashboard.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/graphql/**") // Apply CORS to GraphQL endpoint
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("POST")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}