package com.appointments.domain;

import java.util.Objects;
import java.util.UUID;

public record Appointment(
        UUID id,
        String bookingCode,
        UUID clientId,
        UUID slotId,
        AppointmentStatus status) {

    public Appointment {
        Objects.requireNonNull(id, "Appointment ID must not be null.");
        Objects.requireNonNull(
                bookingCode, "Booking code must not be null.");
        Objects.requireNonNull(clientId, "Client ID must not be null.");
        Objects.requireNonNull(slotId, "Slot ID must not be null.");
        Objects.requireNonNull(status, "Appointment status must not be null.");

        if (bookingCode.isBlank()) {
            throw new IllegalArgumentException(
                    "Booking code must not be blank.");
        }
    }

    public Appointment cancel() {
        return new Appointment(
                id,
                bookingCode,
                clientId,
                slotId,
                AppointmentStatus.CANCELLED);
    }

    public Appointment reschedule(UUID newSlotId) {
        if (status != AppointmentStatus.CONFIRMED) {
            throw new IllegalStateException(
                    "Only confirmed appointments can be rescheduled.");
        }

        return new Appointment(
                id,
                bookingCode,
                clientId,
                newSlotId,
                status);
    }
}