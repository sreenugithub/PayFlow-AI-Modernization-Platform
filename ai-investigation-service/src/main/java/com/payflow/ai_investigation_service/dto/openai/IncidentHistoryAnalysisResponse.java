package com.payflow.ai_investigation_service.dto.openai;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentHistoryAnalysisResponse {

    private Integer totalIncidents;

    private List<String> topRootCauses;

    private List<String> topOwnerTeams;

    private String severityTrend;

    private String aiSummary;

    private String recommendation;
}