package com.appointments.repository;

import com.appointments.domain.AvailabilitySlot;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AvailabilitySlotRepository {

    AvailabilitySlot save(AvailabilitySlot slot);

    Optional<AvailabilitySlot> findById(UUID id);

    List<AvailabilitySlot> findByProfessionalId(UUID professionalId);

    List<AvailabilitySlot> findAll();
}