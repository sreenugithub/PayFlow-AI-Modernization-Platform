package com.payflow.notification_service.consumer;

import com.payflow.notification_service.event.StuckPaymentEvent;
import com.payflow.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StuckPaymentConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "${payment.kafka.topics.payment-stuck}",
            groupId = "notification-stuck-group",
            properties = {
                    "spring.json.value.default.type=com.payflow.notification_service.event.StuckPaymentEvent",
                    "spring.json.use.type.headers=false"
            }
    )
    public void consume(StuckPaymentEvent event) {

        log.warn(
                "Received stuck payment reference={}",
                event.getPaymentReference()
        );

        notificationService.sendStuckPaymentNotification(
                event.getPaymentReference(),
                event.getReason()
        );
    }
}