package com.payflow.payment_service.producer;

import com.payflow.payment_service.event.PaymentCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${payment.kafka.topics.payment-created}")
    private String paymentCreatedTopic;

    public void publishPaymentCreatedEvent(
            PaymentCreatedEvent event) {
        log.info("Published PaymentCreatedEvent reference={}", event.getPaymentReference());
        kafkaTemplate.send(
                paymentCreatedTopic,
                event.getPaymentReference(),
                event
        );
    }
}