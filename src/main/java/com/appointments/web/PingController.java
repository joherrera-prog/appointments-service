package com.appointments.web;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Handle HTTP requests and return response data.
@RestController
public class PingController {

    // Handle GET requests sent to /api/ping.
    @GetMapping("/api/ping")
    public Map<String, String> ping() {
        // Spring converts this map into a JSON response.
        return Map.of("status", "ok");
    }
}