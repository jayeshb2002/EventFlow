package com.eventflow.eventManagement.retry.service;

import com.eventflow.eventManagement.common.dto.IncidentEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class IncidentDLTConsumer {

    @KafkaListener(topics = "incident-events-DLT",
            groupId = "dlt-consumer-group")
    public void consumeDLT(IncidentEvent event) {
        System.err.println("DLT Event: " + event.getEventId());
        // Alerting, manual review, etc.
    }
}
