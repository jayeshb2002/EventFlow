package com.eventflow.eventManagement.incident.controller;

import com.eventflow.eventManagement.common.dto.Incident;
import com.eventflow.eventManagement.common.request.UpdateIncidentRequest;
import com.eventflow.eventManagement.common.utils.RateLimiter;
import com.eventflow.eventManagement.incident.service.IncidentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService service;
    private final RateLimiter rateLimiter;

    public IncidentController(IncidentService service, RateLimiter rateLimiter) {
        this.service = service;
        this.rateLimiter=rateLimiter;
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody Incident incident,
                                       Authentication auth) {
        String key = "rate:incident:create:" + auth.getName();

        if (!rateLimiter.allowRequest(key)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Rate limit exceeded");
        }

        Long id = service.createIncident(incident, auth.getName());
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

    @GetMapping("/{id}")
    public Incident getById(@PathVariable Long id) {
        return service.getIncidentById(id);
    }
}