package com.eventflow.eventManagement.incident.repository;

import com.eventflow.eventManagement.common.dto.Incident;
import com.eventflow.eventManagement.common.mapper.IncidentRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class IncidentRepository {

    private final JdbcTemplate jdbcTemplate;

    public IncidentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long createIncident(Incident incident) {
        String sql = """
            INSERT INTO eventflowdb.incidents (title, description, type, severity, status, created_by)
            VALUES (?, ?, ?, ?, ?, ?)
            RETURNING id
        """;

        return jdbcTemplate.queryForObject(
                sql,
                Long.class,
                incident.getTitle(),
                incident.getDescription(),
                incident.getType(),
                incident.getSeverity(),
                incident.getStatus(),
                incident.getCreatedBy()
        );
    }

    public List<Incident> findAll() {
        String sql = "SELECT * FROM incidents ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new IncidentRowMapper());
    }

    public Optional<Incident> findById(Long id) {
        String sql = "SELECT * FROM incidents WHERE id = ?";
        return jdbcTemplate.query(sql, new IncidentRowMapper(), id)
                .stream().findFirst();
    }

    public void updateIncident(Long id, String status, String severity) {
        String sql = """
        UPDATE incidents
        SET status = ?, severity = ?, updated_at = CURRENT_TIMESTAMP
        WHERE id = ?
    """;
        jdbcTemplate.update(sql, status, severity, id);
    }
}