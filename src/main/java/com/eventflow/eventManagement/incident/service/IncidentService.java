package com.eventflow.eventManagement.incident.service;

import com.eventflow.eventManagement.common.dto.Incident;
import com.eventflow.eventManagement.common.dto.IncidentEvent;
import com.eventflow.eventManagement.common.request.UpdateIncidentRequest;
import com.eventflow.eventManagement.incident.repository.IncidentRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class IncidentService {

    private final IncidentRepository repository;
    private final IncidentEventProducer eventProducer;
    private final RedisTemplate<String, Object> redisTemplate;

    public IncidentService(IncidentRepository repository,
                           RedisTemplate<String, Object> redisTemplate, IncidentEventProducer eventProducer) {
        this.repository = repository;
        this.redisTemplate = redisTemplate;
        this.eventProducer = eventProducer;
    }

    public Long createIncident(Incident incident, String username) {

        Long id = repository.createIncident(incident);

        IncidentEvent event = new IncidentEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setEventType("INCIDENT_CREATED");
        event.setIncidentId(id);
        event.setPerformedBy(username);
        event.setTimestamp(Instant.now());
        event.setPayload(Map.of(
                "severity", incident.getSeverity(),
                "type", incident.getType()
        ));

        eventProducer.publish(event);
        return id;
    }


    public List<Incident> getAllIncidents() {
        return repository.findAll();
    }

    public void updateIncident(Long id, UpdateIncidentRequest req) {
        repository.updateIncident(id, req.getStatus(), req.getSeverity());
    }

    public Incident getIncidentById(Long id) {
        String key = "incident:" + id;

        Incident cached = (Incident) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return cached;
        }

        Incident incident = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        redisTemplate.opsForValue().set(key, incident, Duration.ofMinutes(5));
        return incident;
    }

}