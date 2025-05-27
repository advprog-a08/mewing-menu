package id.ac.ui.cs.advprog.mewingmenu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // allow all paths
                        .allowedOrigins("http://localhost", "http://localhost:3000", "http://rizzserve.site", "https://rizzserve.site")
                        .allowedMethods("*") // allow all HTTP methods (GET, POST, etc.)
                        .allowedHeaders("*"); // allow all headers
            }
        };
    }
}
