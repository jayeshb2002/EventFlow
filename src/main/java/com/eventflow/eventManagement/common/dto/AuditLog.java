package com.eventflow.eventManagement.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {

    private Long id;
    private Long incidentId;
    private String action;
    private String oldValue;
    private String newValue;
    private String changedBy;
    private LocalDateTime createdAt;
}