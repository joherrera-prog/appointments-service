package com.appointments.repository.memory;

import com.appointments.domain.Appointment;
import com.appointments.repository.AppointmentRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class InMemoryAppointmentRepository
        implements AppointmentRepository {

    private final Map<UUID, Appointment> appointmentsById = new HashMap<>();
    private final Map<String, UUID> appointmentIdsByBookingCode = new HashMap<>();

    @Override
    public synchronized Appointment save(Appointment appointment) {
        Objects.requireNonNull(appointment, "Appointment must not be null.");

        UUID existingId = appointmentIdsByBookingCode.get(
                appointment.bookingCode());

        if (existingId != null && !existingId.equals(appointment.id())) {
            throw new IllegalArgumentException(
                    "Booking code is already in use.");
        }

        Appointment previous = appointmentsById.put(
                appointment.id(), appointment);

        if (previous != null) {
            appointmentIdsByBookingCode.remove(previous.bookingCode());
        }

        appointmentIdsByBookingCode.put(
                appointment.bookingCode(), appointment.id());

        return appointment;
    }

    @Override
    public synchronized Optional<Appointment> findById(UUID id) {
        Objects.requireNonNull(id, "Appointment ID must not be null.");
        return Optional.ofNullable(appointmentsById.get(id));
    }

    @Override
    public synchronized Optional<Appointment> findByBookingCode(
            String bookingCode) {
        Objects.requireNonNull(
                bookingCode, "Booking code must not be null.");

        UUID appointmentId = appointmentIdsByBookingCode.get(bookingCode);

        if (appointmentId == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(appointmentsById.get(appointmentId));
    }

    @Override
    public synchronized List<Appointment> findAll() {
        return List.copyOf(appointmentsById.values());
    }

    @Override
    public synchronized boolean deleteById(UUID id) {
        Objects.requireNonNull(id, "Appointment ID must not be null.");

        Appointment removed = appointmentsById.remove(id);

        if (removed == null) {
            return false;
        }

        appointmentIdsByBookingCode.remove(removed.bookingCode());
        return true;
    }
}