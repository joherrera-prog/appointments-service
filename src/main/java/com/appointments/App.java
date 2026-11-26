package com.appointments;

import com.appointments.domain.AvailabilitySlot;
import com.appointments.domain.Professional;
import com.appointments.repository.AvailabilitySlotRepository;
import com.appointments.repository.AppointmentRepository;
import com.appointments.repository.ProfessionalRepository;
import com.appointments.repository.memory.InMemoryAvailabilitySlotRepository;
import com.appointments.repository.memory.InMemoryAppointmentRepository;
import com.appointments.repository.memory.InMemoryProfessionalRepository;
import com.appointments.service.AvailabilityService;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public final class App {

    private App() {
        // Prevent instantiation of the application entry point.
    }

    public static void main(String[] args) {
        // Create repositories for professionals, slots and appointments.
        // The appointment repository maintains two separate lookup indexes.
        ProfessionalRepository professionalRepository =
                new InMemoryProfessionalRepository();
        AvailabilitySlotRepository slotRepository =
                new InMemoryAvailabilitySlotRepository();
        AppointmentRepository appointmentRepository =
                new InMemoryAppointmentRepository();

        // Use a real clock to obtain the current instant.
        Clock clock = Clock.systemUTC();

        // Supply the repositories and clock to the service's constructor.
        AvailabilityService availabilityService = new AvailabilityService(
                professionalRepository, slotRepository, appointmentRepository, clock);

        // Create a professional record and register it in the repository.
        Professional professional = new Professional(
                UUID.randomUUID(),
                "Tattoo artist",
                ZoneId.of("America/Mexico_City"));
        professionalRepository.save(professional);

        // Express the current instant in the professional's zone.
        // Then select tomorrow at exactly 10:00:00, with no fractional seconds.
        ZonedDateTime start = ZonedDateTime.now(clock)
                .withZoneSameInstant(professional.timeZone())
                .plusDays(1)
                .withHour(10)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        // The acting professional and calendar owner are the same person.
        // The service validates the request and stores the published slot.
        AvailabilitySlot publishedSlot = availabilityService.publish(
                professional.id(), professional.id(), start);

        // Display the result of the service operation.
        System.out.println("Professional: " + professional.name());
        System.out.println("Slot ID: " + publishedSlot.id());
        System.out.println("Start: " + publishedSlot.start());
        System.out.println("End: " + publishedSlot.end());
        System.out.println("Published: " + publishedSlot.published());

        // Query the repository to confirm that the new slot was stored.
        System.out.println("Stored slots: "
                + slotRepository.findByProfessionalId(professional.id()).size());

        // Display the public availability query result.
        System.out.println("Available slots:");
        for (AvailabilitySlot slot : availabilityService.findAvailable(professional.id())) {
            System.out.println(slot.start() + " -> " + slot.end());
        }
    }
}
