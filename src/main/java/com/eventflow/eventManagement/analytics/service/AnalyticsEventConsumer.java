package com.eventflow.eventManagement.analytics.service;

import com.eventflow.eventManagement.common.dto.IncidentEvent;
import com.eventflow.eventManagement.analytics.repository.AnalyticsRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AnalyticsEventConsumer {

    private final AnalyticsRepository analyticsRepository;

    public AnalyticsEventConsumer(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    @KafkaListener(topics = "incident-events", groupId = "analytics-consumer-group")
    public void consume(IncidentEvent event) {

        if (!"INCIDENT_CREATED".equals(event.getEventType())) {
            return;
        }

        String severity = (String) event.getPayload().get("severity");
        String type = (String) event.getPayload().get("type");

        analyticsRepository.incrementSeverity(severity);
        analyticsRepository.incrementType(type);
        analyticsRepository.incrementDaily(LocalDate.now());
    }
}