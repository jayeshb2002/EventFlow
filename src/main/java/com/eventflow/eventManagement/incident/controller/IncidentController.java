package com.eventflow.eventManagement.incident.controller;

import com.eventflow.eventManagement.common.dto.Incident;
import com.eventflow.eventManagement.common.request.UpdateIncidentRequest;
import com.eventflow.eventManagement.incident.service.IncidentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService service;

    public IncidentController(IncidentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody Incident incident) {
        Long id = service.createIncident(incident);
        return ResponseEntity.ok(id);
    }

    @GetMapping
    public List<Incident> getAll() {
        return service.getAllIncidents();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody UpdateIncidentRequest req) {
        service.updateIncident(id, req);
        return ResponseEntity.ok().build();
    }
}