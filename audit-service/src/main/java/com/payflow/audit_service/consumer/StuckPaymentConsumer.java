package com.payflow.audit_service.consumer;

import com.payflow.audit_service.entity.PaymentAuditEvent;
import com.payflow.audit_service.event.StuckPaymentEvent;
import com.payflow.audit_service.repository.PaymentAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class StuckPaymentConsumer {

    private final PaymentAuditRepository paymentAuditRepository;

    @KafkaListener(
            topics = "${payment.kafka.topics.payment-stuck}",
            groupId = "audit-stuck-group",
            properties = {
                    "spring.json.value.default.type=com.payflow.audit_service.event.StuckPaymentEvent",
                    "spring.json.use.type.headers=false"
            }
    )
    public void consume(StuckPaymentEvent event) {

        log.warn(
                "STUCK payment received reference={}",
                event.getPaymentReference()
        );

        PaymentAuditEvent auditEvent =
                PaymentAuditEvent.builder()
                        .paymentId(event.getPaymentId())
                        .paymentReference(event.getPaymentReference())
                        .eventType("PAYMENT_STUCK")
                        .status(event.getCurrentStatus())
                        .receivedAt(LocalDateTime.now())
                        .build();

        paymentAuditRepository.save(auditEvent);

        log.warn(
                "STUCK payment stored reference={}",
                event.getPaymentReference()
        );
    }
}