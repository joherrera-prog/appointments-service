package com.appointments.repository.memory;

import com.appointments.domain.Professional;
import com.appointments.repository.ProfessionalRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

// Implements the professional storage contract using an in-memory map.
public final class InMemoryProfessionalRepository
        implements ProfessionalRepository {

    // Associate each professional's ID with their current record.
    private final Map<UUID, Professional> professionalsById = new HashMap<>();

    @Override
    public synchronized Professional save(Professional professional) {
        // Reject a missing object before accessing its ID.
        Objects.requireNonNull(
                professional, "Professional must not be null.");

        // Insert a new professional or replace the existing version.
        professionalsById.put(professional.id(), professional);
        return professional;
    }

    @Override
    public synchronized Optional<Professional> findById(UUID id) {
        Objects.requireNonNull(id, "Professional ID must not be null.");

        // Convert a missing map entry into an empty Optional.
        return Optional.ofNullable(professionalsById.get(id));
    }
}