package net.octoberserver.ordersystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderSystemApplication.class, args);
    }
}

/*
 * DONE: Implement JWT Authentication and Authorization :
 * DONE: Implement Middleware/Filters for Auth
 * DONE: Implement OAuth Callback login (login / register)
 * DONE: Implement OAuth Validation (Determine if it is a school account with the correct class and seat number)
 */
