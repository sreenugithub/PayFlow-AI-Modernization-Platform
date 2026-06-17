package com.payflow.audit_service.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payflow.audit_service.entity.DltEvent;
import com.payflow.audit_service.event.PaymentCreatedEvent;
import com.payflow.audit_service.repository.DltEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCreatedDltConsumer {

    private final DltEventRepository repository;

    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${payment.kafka.topics.payment-created-dlt}",
            groupId = "audit-dlt-group",
            properties = {
                    "spring.json.value.default.type=com.payflow.audit_service.event.PaymentCreatedEvent",
                    "spring.json.use.type.headers=false"
            }
    )
    public void consume(PaymentCreatedEvent payload) {

        try {

            String rawPayload =
                    objectMapper.writeValueAsString(payload);

            DltEvent event =
                    DltEvent.builder()
                            .originalTopic("payment-created-topic")
                            .dltTopic("payment-created-topic-dlt")
                            .paymentReference(payload.getPaymentReference())
                            .payload(rawPayload)
                            .status("UNRESOLVED")
                            .errorReason("Processing failure")
                            .receivedAt(LocalDateTime.now())
                            .build();

            repository.save(event);

            log.error(
                    "DLT event stored successfully"
            );

        } catch (Exception ex) {

            log.error(
                    "Failed to store DLT event",
                    ex
            );
        }
    }
}
