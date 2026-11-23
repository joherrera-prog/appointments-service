package com.appointments.domain;

import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

public record Professional(UUID id, String name, ZoneId timeZone) {

    public Professional {
        Objects.requireNonNull(id, "Professional ID must not be null.");
        Objects.requireNonNull(name, "Professional name must not be null.");
        Objects.requireNonNull(
                timeZone, "Professional time zone must not be null.");

        if (name.isBlank()) {
            throw new IllegalArgumentException(
                    "Professional name must not be blank.");
        }
    }
}