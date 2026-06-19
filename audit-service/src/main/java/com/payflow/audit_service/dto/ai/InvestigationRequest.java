package com.payflow.audit_service.dto.ai;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestigationRequest {

    private String domain;
    private String referenceId;
    private String businessStatus;
    private String investigationStatus;
    private String rootCause;
    private String suggestedAction;
    private List<TimelineEvent> timeline;
}
