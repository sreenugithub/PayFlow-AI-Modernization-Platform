package com.payflow.audit_service.service.ai;

import com.payflow.audit_service.dto.ai.AiInvestigationResponse;
import com.payflow.audit_service.dto.ai.IncidentHistoryAnalysisResponse;
import com.payflow.audit_service.dto.ai.IncidentHistoryRequest;
import com.payflow.audit_service.dto.ai.InvestigationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class AiInvestigationClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${ai-investigation.service.url:http://localhost:8080/api/ai/investigate}")
    private String aiInvestigationUrl;

    @Value("${ai-investigation.service.history-url:http://localhost:8080/api/ai/incident-history-analysis}")
    private String aiIncidentHistoryUrl;

    public AiInvestigationResponse investigate(
            InvestigationRequest request) {

        return webClientBuilder.build()
                .post()
                .uri(aiInvestigationUrl)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AiInvestigationResponse.class)
                .block();
    }

    public IncidentHistoryAnalysisResponse analyzeHistory(
            IncidentHistoryRequest request) {

        return webClientBuilder.build()
                .post()
                .uri(aiIncidentHistoryUrl)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(IncidentHistoryAnalysisResponse.class)
                .block();
    }
}
