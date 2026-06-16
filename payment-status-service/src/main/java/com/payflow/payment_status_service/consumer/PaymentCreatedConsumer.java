package com.payflow.payment_status_service.consumer;

import com.payflow.payment_status_service.event.PaymentCreatedEvent;
import com.payflow.payment_status_service.event.PaymentStatusUpdatedEvent;
import com.payflow.payment_status_service.producer.PaymentStatusPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCreatedConsumer {

    private final PaymentStatusPublisher paymentStatusPublisher;

    @KafkaListener(
            topics = "${payment.kafka.topics.payment-created}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {
                    "spring.json.value.default.type=com.payflow.payment_status_service.event.PaymentCreatedEvent",
                    "spring.json.use.type.headers=false"
            }
    )
    public void consume(PaymentCreatedEvent event) {

        log.info(
                "Payment received for processing reference={}",
                event.getPaymentReference()
        );

        String newStatus;

        if ("URGENT".equalsIgnoreCase(event.getPaymentType())) {
            newStatus = "COMPLETED";
        } else {
            newStatus = "PROCESSING";
        }

        PaymentStatusUpdatedEvent updatedEvent =
                PaymentStatusUpdatedEvent.builder()
                        .paymentId(event.getPaymentId())
                        .paymentReference(event.getPaymentReference())
                        .oldStatus(event.getStatus())
                        .newStatus(newStatus)
                        .build();

        paymentStatusPublisher.publish(updatedEvent);

        log.info(
                "Payment status updated reference={} status={}",
                event.getPaymentReference(),
                newStatus
        );
    }
}
