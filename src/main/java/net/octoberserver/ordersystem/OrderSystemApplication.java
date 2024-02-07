package net.octoberserver.ordersystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class OrderSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderSystemApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/").allowedOrigins("https://zl-order-system.github.io");
            }
        };
    }
}

/*
 * DONE: Implement JWT Authentication and Authorization :
 * DONE: Implement Middleware/Filters for Auth
 * DONE: Implement OAuth Callback login (login / register)
 * DONE: Implement OAuth Validation (Determine if it is a school account with the correct class and seat number)
 * -----------------------------------------------------------------------------------------------------------------------------
 * Security Problems
 * 1. Token does not expire
 *
 */
