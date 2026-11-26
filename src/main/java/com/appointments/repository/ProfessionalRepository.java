package com.appointments.repository;

import com.appointments.domain.Professional;
import java.util.Optional;
import java.util.UUID;

// Defines how the application registers and retrieves professionals.
public interface ProfessionalRepository {

    // Store a professional, replacing the version with the same ID.
    Professional save(Professional professional);

    // Return the registered professional, or an empty result if absent.
    Optional<Professional> findById(UUID id);
}