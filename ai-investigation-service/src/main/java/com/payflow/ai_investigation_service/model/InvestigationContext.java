package com.payflow.ai_investigation_service.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestigationContext {

    private String paymentStatus;
    private String investigationStatus;
    private String rootCause;
    private String suggestedAction;
}