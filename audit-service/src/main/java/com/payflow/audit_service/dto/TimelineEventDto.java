package com.payflow.audit_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimelineEventDto {

    private String eventType;

    private String status;

    private String timestamp;

    private String source;

    private String description;

    private String severity;

    private String recommendation;

}