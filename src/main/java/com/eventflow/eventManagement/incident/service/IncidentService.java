package com.eventflow.eventManagement.incident.service;

import com.eventflow.eventManagement.common.dto.Incident;
import com.eventflow.eventManagement.common.request.UpdateIncidentRequest;
import com.eventflow.eventManagement.incident.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentService {

    private final IncidentRepository repository;

    public IncidentService(IncidentRepository repository) {
        this.repository = repository;
    }

    public Long createIncident(Incident incident) {
        incident.setStatus("OPEN");
        return repository.createIncident(incident);
    }

    public List<Incident> getAllIncidents() {
        return repository.findAll();
    }

    public void updateIncident(Long id, UpdateIncidentRequest req) {
        repository.updateIncident(id, req.getStatus(), req.getSeverity());
    }

}