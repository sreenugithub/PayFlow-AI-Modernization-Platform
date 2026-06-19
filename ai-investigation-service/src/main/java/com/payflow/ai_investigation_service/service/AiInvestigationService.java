package com.payflow.ai_investigation_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payflow.ai_investigation_service.dto.AiInvestigationResponse;
import com.payflow.ai_investigation_service.dto.InvestigationRequest;
import com.payflow.ai_investigation_service.dto.openai.OpenAiRequest;
import com.payflow.ai_investigation_service.dto.openai.OpenAiResponse;
import com.payflow.ai_investigation_service.model.InvestigationContext;
import com.payflow.ai_investigation_service.resolver.OwnerTeamResolver;
import com.payflow.ai_investigation_service.resolver.RecommendationEngine;
import com.payflow.ai_investigation_service.resolver.SeverityResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiInvestigationService {

    private final WebClient.Builder webClientBuilder;
    private final PromptBuilderService promptBuilderService;

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.url}")
    private String openAiUrl;

    private final ObjectMapper objectMapper;
    private final OwnerTeamResolver ownerTeamResolver;
    private final SeverityResolver severityResolver;
    private final RecommendationEngine recommendationEngine;

    public AiInvestigationResponse investigate(InvestigationRequest request) {

        log.info("OpenAI API key configured={}",
                apiKey != null && !apiKey.isBlank());

        InvestigationContext context =
                InvestigationContext.builder()
                        .paymentStatus(request.getBusinessStatus())
                        .investigationStatus(request.getInvestigationStatus())
                        .rootCause(request.getRootCause())
                        .suggestedAction(request.getSuggestedAction())
                        .build();

        String ownerTeam = ownerTeamResolver.resolveOwnerTeam(context);
        String severity = severityResolver.resolveSeverity(context);
        String recommendedAction = recommendationEngine.recommend(context);

        if (apiKey == null || apiKey.isBlank()) {
            log.info( " ------   apiKey is empty   -------");

            return AiInvestigationResponse.builder()
                    .summary("AI analysis completed using deterministic investigation rules.")
                    .probableCause(request.getRootCause())
                    .businessImpact(resolveBusinessImpact(severity))
                    .recommendedAction(recommendedAction)
                    .severity(severity)
                    .ownerTeam(ownerTeam)
                    .confidence(resolveConfidence(severity))
                    .build();
        }

        String prompt = promptBuilderService.buildPrompt(request, ownerTeam, severity, recommendedAction);

        OpenAiRequest openAiRequest = OpenAiRequest.builder()
                .model(model)
                .input(prompt)
                .build();


        log.info("------- openAiRequest  ----");
        log.info(openAiRequest.toString());
        OpenAiResponse openAiResponse;

        try {
            openAiResponse = webClientBuilder.build()
                    .post()
                    .uri(openAiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(openAiRequest)
                    .retrieve()
                    .bodyToMono(OpenAiResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error(
                    "OpenAI request failed status={} body={}",
                    ex.getStatusCode(),
                    ex.getResponseBodyAsString());

            return buildFallbackResponse(
                    request,
                    recommendedAction,
                    severity,
                    ownerTeam,
                    "OpenAI request failed with status "
                            + ex.getStatusCode());
        } catch (Exception ex) {
            log.error("OpenAI request failed", ex);

            return buildFallbackResponse(
                    request,
                    recommendedAction,
                    severity,
                    ownerTeam,
                    "OpenAI request failed: " + ex.getMessage());
        }


        log.info("------- openAiResponse  ----");

        log.info("OpenAI response received={}", openAiResponse != null);

        String aiText = extractText(openAiResponse);

        try {
            AiInvestigationResponse response =
                    objectMapper.readValue(
                    aiText,
                    AiInvestigationResponse.class
            );

            response.setOwnerTeam(ownerTeam);
            response.setSeverity(severity);
            response.setRecommendedAction(recommendedAction);

            return response;
        } catch (Exception ex) {
            return AiInvestigationResponse.builder()
                    .summary(aiText)
                    .probableCause(request.getRootCause())
                    .businessImpact("Unable to parse structured AI response.")
                    .recommendedAction(recommendedAction)
                    .severity(severity)
                    .ownerTeam(ownerTeam)
                    .confidence("LOW")
                    .build();
        }
    }

    private String extractText(OpenAiResponse response) {
        if (response == null || response.getOutput() == null) {
            return "No AI response received.";
        }

        return response.getOutput().stream()
                .filter(output -> output.getContent() != null)
                .flatMap(output -> output.getContent().stream())
                .map(OpenAiResponse.Content::getText)
                .filter(text -> text != null && !text.isBlank())
                .findFirst()
                .orElse("No AI response text received.");
    }

    private AiInvestigationResponse buildFallbackResponse(
            InvestigationRequest request,
            String recommendedAction,
            String severity,
            String ownerTeam,
            String summary) {

        return AiInvestigationResponse.builder()
                .summary(summary)
                .probableCause(request.getRootCause())
                .businessImpact(resolveBusinessImpact(severity))
                .recommendedAction(recommendedAction)
                .severity(severity)
                .ownerTeam(ownerTeam)
                .confidence(resolveConfidence(severity))
                .build();
    }

    private String resolveBusinessImpact(String severity) {
        if ("LOW".equalsIgnoreCase(severity)) {
            return "No current business impact identified.";
        }

        if ("CRITICAL".equalsIgnoreCase(severity)) {
            return "Payment processing requires immediate operational attention.";
        }

        return "Payment processing may require operational review.";
    }

    private String resolveConfidence(String severity) {
        return "LOW".equalsIgnoreCase(severity)
                ? "HIGH"
                : "MEDIUM";
    }
}
