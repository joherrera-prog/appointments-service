package com.appointments.service;

import com.appointments.domain.Professional;
import com.appointments.repository.ProfessionalRepository;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

// Coordinate professional registration and retrieval.
@Service
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;

    // Spring supplies the repository implementation registered as a bean.
    public ProfessionalService(ProfessionalRepository professionalRepository) {
        this.professionalRepository = professionalRepository;
    }

    public Professional create(String name, String timeZone) {
        // Reject missing or blank input before creating or storing a record.
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Professional name must not be blank.");
        }
        if (timeZone == null || timeZone.isBlank()) {
            throw new IllegalArgumentException(
                    "Professional time zone must not be blank.");
        }

        // Convert the supplied text into a valid Java time zone.
        ZoneId zone;
        try {
            zone = ZoneId.of(timeZone);
        } catch (DateTimeException exception) {
            throw new IllegalArgumentException(
                    "Professional time zone is invalid.", exception);
        }

        // Generate the ID in the application and persist the new professional.
        Professional professional = new Professional(UUID.randomUUID(), name, zone);
        return professionalRepository.save(professional);
    }

    public Optional<Professional> findById(UUID id) {
        return professionalRepository.findById(id);
    }
}
