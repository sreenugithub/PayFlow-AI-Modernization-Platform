package com.payflow.payment_status_service.scheduler;

import com.payflow.payment_status_service.event.StuckPaymentEvent;
import com.payflow.payment_status_service.producer.StuckPaymentPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StuckPaymentDetector {

    private final StuckPaymentPublisher stuckPaymentPublisher;

    //@Scheduled(fixedRate = 23460000)
    public void detectStuckPayments() {

        log.info("Running stuck payment detection job");

        StuckPaymentEvent event = StuckPaymentEvent.builder()
                .paymentId("SIM-PAY-001")
                .paymentReference("SIMULATED-STUCK-PAYMENT")
                .currentStatus("PROCESSING")
                .reason("Payment exceeded configured SLA threshold")
                .stuckDurationMinutes(30L)
                .build();

        stuckPaymentPublisher.publish(event);
    }
}