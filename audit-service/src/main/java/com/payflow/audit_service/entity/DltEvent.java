package com.payflow.audit_service.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "dlt_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DltEvent {

    @Id
    private String id;

    private String originalTopic;

    private String dltTopic;

    private String paymentReference;

    private String errorReason;

    private String payload;

    private String status;

    private LocalDateTime receivedAt;

    private String description;
}