package com.eventflow.eventManagement.incident.service;

import com.eventflow.eventManagement.common.dto.IncidentEvent;
import com.eventflow.eventManagement.common.dto.IncidentUpdatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class IncidentEventProducer {

    private final KafkaTemplate<String, IncidentEvent> kafkaCreateTemplate;
    private final KafkaTemplate<String, IncidentUpdatedEvent> kafkaUpdateTemplate;
    private static final String CREATE_TOPIC = "incident-events";
    private static final String UPDATE_TOPIC = "incident-updated";

    public IncidentEventProducer(KafkaTemplate<String, IncidentEvent> kafkaCreateTemplate,
                                 KafkaTemplate<String, IncidentUpdatedEvent> kafkaUpdateTemplate) {
        this.kafkaCreateTemplate = kafkaCreateTemplate;
        this.kafkaUpdateTemplate = kafkaUpdateTemplate;
    }

    public void publish(IncidentEvent event) {
        kafkaCreateTemplate.send(CREATE_TOPIC, event.getEventId(), event);
    }

    public void sendIncidentUpdatedEvent(IncidentUpdatedEvent incident) {
        kafkaUpdateTemplate.send(UPDATE_TOPIC, incident);
    }
}
