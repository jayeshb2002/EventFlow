package com.eventflow.eventManagement.audit.service;

import com.eventflow.eventManagement.audit.repository.AuditRepository;
import com.eventflow.eventManagement.common.dto.IncidentUpdatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class IncidentAuditConsumer {

    private final AuditRepository auditRepository;

    public IncidentAuditConsumer(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @KafkaListener(topics = "incident-updated", groupId = "audit-group")
    public void consume(IncidentUpdatedEvent event) {

        auditRepository.save(
                event.getIncidentId(),
                event.getField(),
                event.getOldValue(),
                event.getNewValue(),
                event.getUpdatedBy()
        );
    }

}