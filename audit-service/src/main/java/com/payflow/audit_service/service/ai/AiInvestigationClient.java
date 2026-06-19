package com.payflow.audit_service.service.ai;

import com.payflow.audit_service.dto.ai.AiInvestigationResponse;
import com.payflow.audit_service.dto.ai.InvestigationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class AiInvestigationClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${ai-investigation.service.url:http://localhost:8086/api/ai/investigate}")
    private String aiInvestigationUrl;

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
}
