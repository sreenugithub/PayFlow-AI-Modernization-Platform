package com.payflow.notification_service.consumer;

import com.payflow.notification_service.event.PaymentStatusUpdatedEvent;
import com.payflow.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentStatusUpdatedConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "${payment.kafka.topics.payment-status-updated}",
            groupId = "notification-status-group",
            properties = {
                    "spring.json.value.default.type=com.payflow.notification_service.event.PaymentStatusUpdatedEvent",
                    "spring.json.use.type.headers=false"
            }
    )
    public void consume(PaymentStatusUpdatedEvent event) {

        log.info(
                "Received status update reference={} status={}",
                event.getPaymentReference(),
                event.getNewStatus()
        );

        if ("COMPLETED".equalsIgnoreCase(event.getNewStatus())) {

            notificationService.sendPaymentCompletedNotification(
                    event.getPaymentReference()
            );
        }
    }
}