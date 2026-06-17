package com.payflow.audit_service.consumer;

import com.payflow.audit_service.entity.PaymentAuditEvent;
import com.payflow.audit_service.event.PaymentCreatedEvent;
import com.payflow.audit_service.repository.PaymentAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCreatedConsumer {

    private final PaymentAuditRepository paymentAuditRepository;

    @KafkaListener(
            topics = "${payment.kafka.topics.payment-created}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {
                    "spring.json.value.default.type=com.payflow.audit_service.event.PaymentCreatedEvent",
                    "spring.json.use.type.headers=false"
            }
    )
    public void consume(PaymentCreatedEvent event) {

        log.info("Audit event received for paymentReference={}",
                event.getPaymentReference());

        if (event.getAmount() > 5000) {
            throw new RuntimeException("Simulated processing failure");
        }

        PaymentAuditEvent auditEvent =
                PaymentAuditEvent.builder()
                        .paymentId(event.getPaymentId())
                        .paymentReference(event.getPaymentReference())
                        .eventType("PAYMENT_CREATED")
                        .paymentType(event.getPaymentType())
                        .amount(event.getAmount())
                        .status(event.getStatus())
                        .receivedAt(LocalDateTime.now())
                        .build();

        paymentAuditRepository.save(auditEvent);

        log.info("Audit event stored for paymentReference={}",
                event.getPaymentReference());
    }
}
