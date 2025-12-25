package com.eventflow.eventManagement.incident.service;

import com.eventflow.eventManagement.common.dto.IncidentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class IncidentEventProducer {

    private final KafkaTemplate<String, IncidentEvent> kafkaTemplate;
    private static final String TOPIC = "incident-events";

    public IncidentEventProducer(KafkaTemplate<String, IncidentEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(IncidentEvent event) {
        kafkaTemplate.send(TOPIC, event.getEventId(), event);
    }
}
