package com.eventflow.eventManagement.common.dto;

import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class IncidentEvent {
    private String eventId;
    private String eventType; // INCIDENT_CREATED, INCIDENT_UPDATED
    private Long incidentId;
    private String performedBy;
    private Instant timestamp;
    private Map<String, Object> payload;
}

