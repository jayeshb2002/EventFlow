package com.eventflow.eventManagement.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncidentUpdatedEvent {
    private Long incidentId;
    private String field;
    private String oldValue;
    private String newValue;
    private String updatedBy;
    private LocalDateTime timestamp;
}