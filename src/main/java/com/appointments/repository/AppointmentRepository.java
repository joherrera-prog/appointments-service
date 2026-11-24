package com.appointments.repository;

import com.appointments.domain.Appointment;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository {

    Appointment save(Appointment appointment);

    Optional<Appointment> findById(UUID id);

    Optional<Appointment> findByBookingCode(String bookingCode);

    List<Appointment> findAll();

    boolean deleteById(UUID id);
}