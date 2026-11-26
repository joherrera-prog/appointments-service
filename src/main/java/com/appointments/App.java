package com.appointments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Enable automatic configuration and component discovery.
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        // Start Spring and the embedded HTTP server.
        SpringApplication.run(App.class, args);
    }
}