package com.payflow.notification_service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    public void sendPaymentCompletedNotification(String paymentReference) {

        log.info(
                "NOTIFICATION SENT : Payment {} completed successfully",
                paymentReference
        );
    }

    public void sendStuckPaymentNotification(
            String paymentReference,
            String reason) {

        log.warn(
                "NOTIFICATION SENT : Payment {} requires investigation. Reason={}",
                paymentReference,
                reason
        );
    }
}