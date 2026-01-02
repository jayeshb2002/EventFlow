package com.eventflow.eventManagement.analytics.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class AnalyticsRepository {

    private final JdbcTemplate jdbcTemplate;

    public AnalyticsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void incrementSeverity(String severity) {
        String sql = """
            INSERT INTO incident_severity_stats (severity, count)
            VALUES (?, 1)
            ON CONFLICT (severity)
            DO UPDATE SET count = incident_severity_stats.count + 1
        """;
        jdbcTemplate.update(sql, severity);
    }

    public void incrementType(String type) {
        String sql = """
            INSERT INTO incident_type_stats (type, count)
            VALUES (?, 1)
            ON CONFLICT (type)
            DO UPDATE SET count = incident_type_stats.count + 1
        """;
        jdbcTemplate.update(sql, type);
    }

    public void incrementDaily(LocalDate date) {
        String sql = """
            INSERT INTO daily_incident_stats (date, total)
            VALUES (?, 1)
            ON CONFLICT (date)
            DO UPDATE SET total = daily_incident_stats.total + 1
        """;
        jdbcTemplate.update(sql, date);
    }
}