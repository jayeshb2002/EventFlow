package com.eventflow.eventManagement.common.request;

import lombok.Data;

@Data
public class UpdateIncidentRequest {
    private String status;
    private String severity;
}