package com.appointments.repository.jdbc;

import com.appointments.domain.Professional;
import com.appointments.repository.ProfessionalRepository;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

// Register this class as a repository managed by Spring.
@Repository
public class JdbcProfessionalRepository implements ProfessionalRepository {

    // Execute SQL statements using Spring's JDBC support.
    private final JdbcClient jdbcClient;

    // Spring supplies the required JdbcClient dependency.
    public JdbcProfessionalRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Professional save(Professional professional) {
        Objects.requireNonNull(
                professional, "Professional must not be null.");

        // Insert a new professional or update the one with the same ID.
        String sql = """
                INSERT INTO professionals (id, name, time_zone)
                VALUES (:id, :name, :timeZone)
                ON CONFLICT(id) DO UPDATE SET
                    name = excluded.name,
                    time_zone = excluded.time_zone
                """;

        // Bind Java values to the named SQL parameters.
        // Store UUID and ZoneId values as text.
        jdbcClient.sql(sql)
                .param("id", professional.id().toString())
                .param("name", professional.name())
                .param("timeZone", professional.timeZone().getId())
                .update();

        return professional;
    }

    @Override
    public Optional<Professional> findById(UUID id) {
        Objects.requireNonNull(id, "Professional ID must not be null.");

        // Retrieve the row belonging to the requested professional.
        String sql = """
                SELECT id, name, time_zone
                FROM professionals
                WHERE id = :id
                """;

        return jdbcClient.sql(sql)
                .param("id", id.toString())
                // Convert a database row into a Professional record.
                .query((resultSet, rowNumber) -> new Professional(
                        UUID.fromString(resultSet.getString("id")),
                        resultSet.getString("name"),
                        ZoneId.of(resultSet.getString("time_zone"))))
                // Return an empty Optional when no matching row exists.
                .optional();
    }
}