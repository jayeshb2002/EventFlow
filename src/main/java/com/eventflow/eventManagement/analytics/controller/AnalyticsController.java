package com.eventflow.eventManagement.analytics.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final JdbcTemplate jdbcTemplate;

    public AnalyticsController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/severity")
    public List<Map<String, Object>> severityStats() {
        return jdbcTemplate.queryForList(
                "SELECT severity, count FROM incident_severity_stats"
        );
    }

    @GetMapping("/type")
    public List<Map<String, Object>> typeStats() {
        return jdbcTemplate.queryForList(
                "SELECT type, count FROM incident_type_stats"
        );
    }

    @GetMapping("/daily")
    public List<Map<String, Object>> dailyStats() {
        return jdbcTemplate.queryForList(
                "SELECT date, total FROM daily_incident_stats ORDER BY date"
        );
    }
}