package com.payflow.ai_investigation_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimelineEvent {

    private String eventType;
    private String status;
    private String timestamp;
    private String source;
    private String description;
}