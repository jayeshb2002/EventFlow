package com.eventflow.eventManagement.audit.repository;

import com.eventflow.eventManagement.common.dto.AuditLog;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuditRepository {

    private final JdbcTemplate jdbcTemplate;

    public AuditRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Long incidentId, String action,
                     String oldValue, String newValue, String user) {

        jdbcTemplate.update("""
          INSERT INTO incident_audit_logs
          (incident_id, action, old_value, new_value, changed_by)
          VALUES (?,?,?,?,?)
        """, incidentId, action, oldValue, newValue, user);
    }

    public List<AuditLog> findByIncidentId(Long incidentId) {

        String sql = """
            SELECT id, incident_id, action, old_value, new_value,
                   changed_by, created_at
            FROM incident_audit_logs
            WHERE incident_id = ?
            ORDER BY created_at ASC
        """;

        return jdbcTemplate.query(sql, new Object[]{incidentId}, (rs, rowNum) -> {
            AuditLog log = new AuditLog();
            log.setId(rs.getLong("id"));
            log.setIncidentId(rs.getLong("incident_id"));
            log.setAction(rs.getString("action"));
            log.setOldValue(rs.getString("old_value"));
            log.setNewValue(rs.getString("new_value"));
            log.setChangedBy(rs.getString("changed_by"));
            log.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return log;
        });
    }
}