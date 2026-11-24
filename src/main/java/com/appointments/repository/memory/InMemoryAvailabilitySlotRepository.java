package com.appointments.repository.memory;

import com.appointments.domain.AvailabilitySlot;
import com.appointments.repository.AvailabilitySlotRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class InMemoryAvailabilitySlotRepository
        implements AvailabilitySlotRepository {

    private final Map<UUID, AvailabilitySlot> slotsById = new HashMap<>();

    @Override
    public synchronized AvailabilitySlot save(AvailabilitySlot slot) {
        Objects.requireNonNull(slot, "Slot must not be null.");
        slotsById.put(slot.id(), slot);
        return slot;
    }

    @Override
    public synchronized Optional<AvailabilitySlot> findById(UUID id) {
        Objects.requireNonNull(id, "Slot ID must not be null.");
        return Optional.ofNullable(slotsById.get(id));
    }

    @Override
    public synchronized List<AvailabilitySlot> findByProfessionalId(
            UUID professionalId) {
        Objects.requireNonNull(
                professionalId, "Professional ID must not be null.");

        return slotsById.values().stream()
                .filter(slot -> slot.professionalId().equals(professionalId))
                .toList();
    }

    @Override
    public synchronized List<AvailabilitySlot> findAll() {
        return List.copyOf(slotsById.values());
    }
}