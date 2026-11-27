package com.appointments.web;

import com.appointments.domain.Professional;
import com.appointments.service.ProfessionalService;
import java.net.URI;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/professionals")
public class ProfessionalController {

    private final ProfessionalService professionalService;

    // Spring supplies the service through constructor injection.
    public ProfessionalController(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    @PostMapping
    public ResponseEntity<Professional> create(
            @RequestBody CreateProfessionalRequest request) {
        // Convert the request into a domain operation through the service.
        Professional professional = professionalService.create(
                request.name(), request.timeZone());

        // Return 201 Created and the URL of the new professional.
        URI location = URI.create("/api/professionals/" + professional.id());
        return ResponseEntity.created(location).body(professional);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Professional> findById(@PathVariable("id") UUID id) {
        // Return 200 with the record, or 404 if the Optional is empty.
        return ResponseEntity.of(professionalService.findById(id));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidInput(IllegalArgumentException exception) {
        // Translate invalid input into an HTTP 400 response with a message.
        return Map.of("message", exception.getMessage());
    }
}
