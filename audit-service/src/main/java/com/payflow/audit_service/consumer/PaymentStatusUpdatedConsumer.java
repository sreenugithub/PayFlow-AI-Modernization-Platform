package com.payflow.audit_service.consumer;

import com.payflow.audit_service.entity.PaymentAuditEvent;
import com.payflow.audit_service.event.PaymentStatusUpdatedEvent;
import com.payflow.audit_service.repository.PaymentAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentStatusUpdatedConsumer {

    private final PaymentAuditRepository paymentAuditRepository;

    @KafkaListener(
            topics = "${payment.kafka.topics.payment-status-updated}",
            groupId = "audit-status-group",
            properties = {
                    "spring.json.value.default.type=com.payflow.audit_service.event.PaymentStatusUpdatedEvent",
                    "spring.json.use.type.headers=false"
            }
    )
    public void consume(PaymentStatusUpdatedEvent event) {
        log.info("Status update audit received reference={} status={}",

                event.getPaymentReference(),

                event.getNewStatus());

        PaymentAuditEvent auditEvent =
                PaymentAuditEvent.builder()
                        .paymentId(event.getPaymentId())
                        .paymentReference(event.getPaymentReference())
                        .eventType("PAYMENT_STATUS_UPDATED")
                        .oldStatus(event.getOldStatus())
                        .newStatus(event.getNewStatus())
                        .receivedAt(LocalDateTime.now())
                        .build();

        paymentAuditRepository.save(auditEvent);

        log.info(
                "Status update audit stored reference={}",
                event.getPaymentReference()
        );
    }
}