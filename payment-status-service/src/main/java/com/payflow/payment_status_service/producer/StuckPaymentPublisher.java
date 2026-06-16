package com.payflow.payment_status_service.producer;

import com.payflow.payment_status_service.event.StuckPaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StuckPaymentPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${payment.kafka.topics.payment-stuck}")
    private String paymentStuckTopic;

    public void publish(StuckPaymentEvent event) {
        kafkaTemplate.send(
                paymentStuckTopic,
                event.getPaymentReference(),
                event
        );

        log.warn(
                "Published StuckPaymentEvent reference={} reason={}",
                event.getPaymentReference(),
                event.getReason()
        );
    }
}