package com.appointments.web;

// Represent the JSON fields accepted when creating a professional.
// The application generates the ID, so callers do not provide it here.
public record CreateProfessionalRequest(String name, String timeZone) {
}
