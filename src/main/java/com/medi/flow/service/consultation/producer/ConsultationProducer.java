package com.medi.flow.service.consultation.producer;

import com.medi.flow.dto.consultation.ConsultationEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ConsultationProducer {

    private static final Logger logger = LoggerFactory.getLogger(ConsultationProducer.class);

    private static final String TOPIC = "consultation-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ConsultationProducer(final KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(ConsultationEventDTO event) {
        kafkaTemplate.send(TOPIC, event)
                .whenComplete((result, throwable) -> {
                    if (throwable == null) {
                        System.out.println("Mensagem enviada com sucesso para o tópico " + result);
                    } else {
                        logger.info("Evento não enviada", throwable);
                    }
                });
    }

}
