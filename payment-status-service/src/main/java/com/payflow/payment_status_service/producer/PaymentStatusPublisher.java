package com.payflow.payment_status_service.producer;

import com.payflow.payment_status_service.event.PaymentStatusUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentStatusPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${payment.kafka.topics.payment-status-updated}")
    private String paymentStatusUpdatedTopic;

    public void publish(PaymentStatusUpdatedEvent event) {

        kafkaTemplate.send(
                paymentStatusUpdatedTopic,
                event.getPaymentReference(),
                event
        );

        log.info(
                "Published PaymentStatusUpdatedEvent reference={} status={}",
                event.getPaymentReference(),
                event.getNewStatus()
        );
    }
}