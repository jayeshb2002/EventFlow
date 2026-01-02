package com.eventflow.eventManagement.common.config;

import com.eventflow.eventManagement.common.dto.IncidentEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, IncidentEvent>
    kafkaListenerContainerFactory(
            ConsumerFactory<String, IncidentEvent> consumerFactory,
            KafkaTemplate<String, IncidentEvent> kafkaTemplate
    ) {

        ConcurrentKafkaListenerContainerFactory<String, IncidentEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        factory.setCommonErrorHandler(
                new DefaultErrorHandler(
                        new DeadLetterPublishingRecoverer(kafkaTemplate),
                        new FixedBackOff(2000L, 3)
                )
        );

        return factory;
    }

    @Bean
    public KafkaTemplate<String, IncidentEvent> kafkaTemplate(
            ProducerFactory<String, IncidentEvent> producerFactory) {

        return new KafkaTemplate<>(producerFactory);
    }
}
