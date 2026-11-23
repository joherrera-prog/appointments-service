package com.appointments.domain;

import java.util.Objects;
import java.util.UUID;

public record Client(UUID id, String name, String email) {

    public Client {
        Objects.requireNonNull(id, "Client ID must not be null.");
        Objects.requireNonNull(name, "Client name must not be null.");
        Objects.requireNonNull(email, "Client email must not be null.");

        if (name.isBlank()) {
            throw new IllegalArgumentException(
                    "Client name must not be blank.");
        }

        if (email.isBlank()) {
            throw new IllegalArgumentException(
                    "Client email must not be blank.");
        }
    }
}