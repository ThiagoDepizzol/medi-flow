package com.medi.flow.service.consultation.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ConsultationProducer {

    private static final Logger logger = LoggerFactory.getLogger(ConsultationProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "consultation-events";

    public ConsultationProducer(final KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Object event) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(TOPIC, event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                logger.error("Error sending consultation event -> {}", ex.getMessage());
            } else {
                logger.error("Consultation created and sent successfully -> {}", TOPIC);
            }
        });
    }
}
