package com.appointments.domain;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

public record AvailabilitySlot(
        UUID id,
        UUID professionalId,
        ZonedDateTime start,
        boolean published) {

    public AvailabilitySlot {
        Objects.requireNonNull(id, "Slot ID must not be null.");
        Objects.requireNonNull(
                professionalId, "Professional ID must not be null.");
        Objects.requireNonNull(start, "Slot start must not be null.");

        if (start.getMinute() != 0
                || start.getSecond() != 0
                || start.getNano() != 0) {
            throw new IllegalArgumentException(
                    "Slots must start on the hour.");
        }
    }

    public ZonedDateTime end() {
        return start.plusHours(1);
    }

    public AvailabilitySlot publish() {
        return new AvailabilitySlot(id, professionalId, start, true);
    }

    public AvailabilitySlot withdraw() {
        return new AvailabilitySlot(id, professionalId, start, false);
    }
}