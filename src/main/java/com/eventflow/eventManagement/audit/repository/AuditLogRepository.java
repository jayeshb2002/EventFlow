package com.eventflow.eventManagement.audit.repository;

import com.eventflow.eventManagement.common.dto.IncidentEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Repository
public class AuditLogRepository {

    private final JdbcTemplate jdbcTemplate;

    public AuditLogRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(IncidentEvent event) {
        String sql = """
            INSERT INTO audit_logs (event_id, event_type, entity_id, entity_type, performed_by, payload)
            VALUES (?, ?, ?, ?, ?, ?::jsonb)
        """;

        jdbcTemplate.update(sql,
                UUID.fromString(event.getEventId()),
                event.getEventType(),
                event.getIncidentId(),
                "INCIDENT",
                event.getPerformedBy(),
                new ObjectMapper().writeValueAsString(event.getPayload())
        );
    }
}