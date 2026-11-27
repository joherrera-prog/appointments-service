package com.appointments.service;

import com.appointments.domain.AvailabilitySlot;
import com.appointments.domain.Appointment;
import com.appointments.domain.AppointmentStatus;
import com.appointments.domain.Professional;
import com.appointments.repository.AppointmentRepository;
import com.appointments.repository.AvailabilitySlotRepository;
import com.appointments.repository.ProfessionalRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

// Coordinates publication and public queries of a professional's availability.
public final class AvailabilityService {

    // Depend on storage contracts rather than concrete HashMap implementations.
    private final ProfessionalRepository professionalRepository;
    private final AvailabilitySlotRepository slotRepository;
    private final AppointmentRepository appointmentRepository;

    // Obtain the current instant from a replaceable clock for predictable tests.
    private final Clock clock;

    public AvailabilityService(
            ProfessionalRepository professionalRepository,
            AvailabilitySlotRepository slotRepository,
            AppointmentRepository appointmentRepository,
            Clock clock) {

        // The caller supplies the collaborators: constructor dependency injection.
        this.professionalRepository = Objects.requireNonNull(
                professionalRepository, "Professional repository must not be null.");
        this.slotRepository = Objects.requireNonNull(
                slotRepository, "Slot repository must not be null.");
        this.clock = Objects.requireNonNull(clock, "Clock must not be null.");
        this.appointmentRepository = Objects.requireNonNull(
                appointmentRepository, "Appointment repository must not be null.");
    }

    public AvailabilitySlot publish(
            UUID actingProfessionalId,
            UUID calendarProfessionalId,
            ZonedDateTime start) {

        Objects.requireNonNull(
                actingProfessionalId, "Acting professional ID must not be null.");
        Objects.requireNonNull(
                calendarProfessionalId, "Calendar professional ID must not be null.");
        Objects.requireNonNull(start, "Slot start must not be null.");

        // A professional may publish only in their own calendar.
        if (!actingProfessionalId.equals(calendarProfessionalId)) {
            throw new IllegalArgumentException(
                    "Professionals can only publish slots in their own calendar.");
        }

        // An empty Optional means that this professional is not registered.
        Professional professional = professionalRepository
                .findById(calendarProfessionalId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Professional does not exist."));

        // Preserve the instant while expressing it in the professional's zone.
        ZonedDateTime professionalStart = start.withZoneSameInstant(
                professional.timeZone());

        // Construction also validates that the local start is on the hour.
        // This candidate is not stored until all remaining checks succeed.
        AvailabilitySlot candidate = new AvailabilitySlot(
                UUID.randomUUID(), professional.id(), professionalStart, true);

        // Keep the duplicate check and save together for this shared repository.
        synchronized (slotRepository) {
            if (!professionalStart.toInstant().isAfter(clock.instant())) {
                throw new IllegalArgumentException(
                        "Slots must start in the future.");
            }

            for (AvailabilitySlot existing
                    : slotRepository.findByProfessionalId(professional.id())) {

                // isEqual compares instants, even when their zone labels differ.
                if (existing.start().isEqual(professionalStart)) {
                    if (existing.published()) {
                        throw new IllegalArgumentException(
                                "A published slot already exists at this time.");
                    }

                    // Republish a withdrawn slot while retaining its original ID.
                    return slotRepository.save(existing.publish());
                }
            }

            // No slot exists for this professional and instant: store a new one.
            return slotRepository.save(candidate);
        }
    }

    // Return future, published, unoccupied slots in chronological order.
    public List<AvailabilitySlot> findAvailable(UUID professionalId) {
        Objects.requireNonNull(
                professionalId, "Professional ID must not be null.");

        Professional professional = professionalRepository
                .findById(professionalId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Professional does not exist."));

        // Read both repositories under a consistent lock order.
        synchronized (slotRepository) {
            synchronized (appointmentRepository) {
                Instant now = clock.instant();

                // Only confirmed appointments occupy their slots.
                // A set stores IDs without duplicates and supports fast membership checks.
                Set<UUID> occupiedSlotIds = new HashSet<>();
                for (Appointment appointment : appointmentRepository.findAll()) {
                    if (appointment.status() == AppointmentStatus.CONFIRMED) {
                        occupiedSlotIds.add(appointment.slotId());
                    }
                }

                return slotRepository.findByProfessionalId(professionalId).stream()
                        .filter(slot -> slot.published())
                        .filter(slot -> slot.start().toInstant().isAfter(now))
                        .filter(slot -> !occupiedSlotIds.contains(slot.id()))
                        // Return each slot expressed in the professional's current zone.
                        .map(slot -> new AvailabilitySlot(
                                slot.id(),
                                slot.professionalId(),
                                slot.start().withZoneSameInstant(professional.timeZone()),
                                slot.published()))
                        .sorted(Comparator.comparing(slot -> slot.start().toInstant()))
                        .toList();
            }
        }
    }
}
