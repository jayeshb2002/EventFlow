package com.eventflow.eventManagement.incident.service;

import com.eventflow.eventManagement.audit.repository.AuditLogRepository;
import com.eventflow.eventManagement.common.dto.IncidentEvent;
import com.eventflow.eventManagement.incident.repository.ProcessedEventRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IncidentEventConsumer {

    private final AuditLogRepository auditLogRepository;
    private final ProcessedEventRepository processedEventRepository;

    public IncidentEventConsumer(AuditLogRepository auditLogRepository,
                                 ProcessedEventRepository processedEventRepository) {
        this.auditLogRepository = auditLogRepository;
        this.processedEventRepository = processedEventRepository;
    }

    @KafkaListener(topics = "incident-events", groupId = "audit-consumer-group")
    public void consume(IncidentEvent event) {

        UUID eventId = UUID.fromString(event.getEventId());

        if (processedEventRepository.alreadyProcessed(eventId)) {
            return;
        }

        auditLogRepository.save(event);
        processedEventRepository.markProcessed(eventId);
    }
}