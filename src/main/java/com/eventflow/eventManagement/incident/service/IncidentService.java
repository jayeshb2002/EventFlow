package com.eventflow.eventManagement.incident.service;

import com.eventflow.eventManagement.common.dto.Incident;
import com.eventflow.eventManagement.common.dto.IncidentEvent;
import com.eventflow.eventManagement.common.dto.IncidentUpdatedEvent;
import com.eventflow.eventManagement.common.request.UpdateIncidentRequest;
import com.eventflow.eventManagement.incident.repository.IncidentRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
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

    @Transactional
    public void updateIncident(Long id, UpdateIncidentRequest req) {

        Incident existing = repository.findByIdForUpdation(id);

        repository.updateIncident(id, req.getStatus(), req.getSeverity());

        IncidentUpdatedEvent event = new IncidentUpdatedEvent();
        event.setIncidentId(id);
        event.setField("STATUS");
        event.setOldValue(existing.getStatus());
        event.setNewValue(req.getStatus());
        event.setUpdatedBy(SecurityContextHolder.getContext()
                .getAuthentication().getName());
        event.setTimestamp(LocalDateTime.now());

        eventProducer.sendIncidentUpdatedEvent(event);
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