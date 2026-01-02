package com.eventflow.eventManagement.incident.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class ProcessedEventRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProcessedEventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean alreadyProcessed(UUID eventId) {
        String sql = "SELECT COUNT(1) FROM processed_events WHERE event_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, eventId);
        return count != null && count > 0;
    }

    public void markProcessed(UUID eventId) {
        jdbcTemplate.update(
                "INSERT INTO processed_events (event_id) VALUES (?)",
                eventId
        );
    }
}