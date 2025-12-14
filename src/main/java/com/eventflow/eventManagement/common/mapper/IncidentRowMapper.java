package com.eventflow.eventManagement.common.mapper;

import com.eventflow.eventManagement.common.dto.Incident;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IncidentRowMapper implements RowMapper<Incident> {

    @Override
    public Incident mapRow(ResultSet rs, int rowNum) throws SQLException {
        Incident incident = new Incident();
        incident.setId(rs.getLong("id"));
        incident.setTitle(rs.getString("title"));
        incident.setDescription(rs.getString("description"));
        incident.setType(rs.getString("type"));
        incident.setSeverity(rs.getString("severity"));
        incident.setStatus(rs.getString("status"));
        incident.setCreatedBy(rs.getLong("created_by"));
        incident.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        incident.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return incident;
    }
}