package com.payflow.ai_investigation_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payflow.ai_investigation_service.dto.AiInvestigationResponse;
import com.payflow.ai_investigation_service.dto.InvestigationRequest;
import com.payflow.ai_investigation_service.dto.openai.*;
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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public IncidentHistoryAnalysisResponse analyzeHistory(
            IncidentHistoryRequest request) {

        List<IncidentSummary> incidents =
                request.getIncidents() == null
                        ? List.of()
                        : request.getIncidents()
                        .stream()
                        .limit(50)
                        .toList();

        List<String> topRootCauses =
                topValues(
                        incidents.stream()
                                .map(IncidentSummary::getRootCause)
                                .toList());

        List<String> topOwnerTeams =
                topValues(
                        incidents.stream()
                                .map(IncidentSummary::getOwnerTeam)
                                .toList());

        String severityTrend =
                resolveSeverityTrend(incidents);

        IncidentHistoryAnalysisResponse response =
                IncidentHistoryAnalysisResponse.builder()
                        .totalIncidents(incidents.size())
                        .topRootCauses(topRootCauses)
                        .topOwnerTeams(topOwnerTeams)
                        .severityTrend(severityTrend)
                        .aiSummary(buildHistorySummary(
                                incidents,
                                topRootCauses))
                        .recommendation(buildHistoryRecommendation(
                                topRootCauses,
                                topOwnerTeams,
                                severityTrend))
                        .build();

        if (apiKey == null || apiKey.isBlank() || incidents.isEmpty()) {
            return response;
        }

        String prompt =
                promptBuilderService.buildIncidentHistoryPrompt(
                        IncidentHistoryRequest.builder()
                                .incidents(incidents)
                                .build(),
                        topRootCauses,
                        topOwnerTeams,
                        severityTrend);

        OpenAiRequest openAiRequest =
                OpenAiRequest.builder()
                        .model(model)
                        .input(prompt)
                        .build();

        try {
            OpenAiResponse openAiResponse =
                    webClientBuilder.build()
                            .post()
                            .uri(openAiUrl)
                            .header("Authorization", "Bearer " + apiKey)
                            .header("Content-Type", "application/json")
                            .bodyValue(openAiRequest)
                            .retrieve()
                            .bodyToMono(OpenAiResponse.class)
                            .block();

            String aiText =
                    extractText(openAiResponse);

            IncidentHistoryAnalysisResponse aiResponse =
                    objectMapper.readValue(
                            aiText,
                            IncidentHistoryAnalysisResponse.class);

            if (aiResponse.getAiSummary() != null
                    && !aiResponse.getAiSummary().isBlank()) {
                response.setAiSummary(
                        aiResponse.getAiSummary());
            }

            if (aiResponse.getRecommendation() != null
                    && !aiResponse.getRecommendation().isBlank()) {
                response.setRecommendation(
                        aiResponse.getRecommendation());
            }

            return response;
        } catch (WebClientResponseException ex) {
            log.error(
                    "OpenAI incident history request failed status={} body={}",
                    ex.getStatusCode(),
                    ex.getResponseBodyAsString());

            return response;
        } catch (Exception ex) {
            log.error(
                    "OpenAI incident history request failed",
                    ex);

            return response;
        }
    }

    private List<String> topValues(
            List<String> values) {

        return values.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(value ->
                        !value.isBlank())
                .collect(Collectors.groupingBy(
                        value -> value,
                        Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry
                        .<String, Long>comparingByValue()
                        .reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(2)
                .map(Map.Entry::getKey)
                .toList();
    }

    private String resolveSeverityTrend(
            List<IncidentSummary> incidents) {

        if (incidents.isEmpty()) {
            return "NO_DATA";
        }

        long highRiskIncidents =
                incidents.stream()
                        .map(IncidentSummary::getSeverity)
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(severity ->
                                "HIGH".equalsIgnoreCase(severity)
                                        || "CRITICAL".equalsIgnoreCase(severity))
                        .count();

        long mediumRiskIncidents =
                incidents.stream()
                        .map(IncidentSummary::getSeverity)
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(severity ->
                                "MEDIUM".equalsIgnoreCase(severity)
                                        || "WARNING".equalsIgnoreCase(severity))
                        .count();

        double highRiskRatio =
                (double) highRiskIncidents / incidents.size();

        double mediumOrHighRiskRatio =
                (double) (highRiskIncidents + mediumRiskIncidents)
                        / incidents.size();

        if (highRiskRatio >= 0.30) {
            return "HIGH_RISK";
        }

        if (mediumOrHighRiskRatio >= 0.40) {
            return "MEDIUM_RISK";
        }

        return "NORMAL";
    }

    private String buildHistorySummary(
            List<IncidentSummary> incidents,
            List<String> topRootCauses) {

        if (incidents.isEmpty()) {
            return "No incident history was provided for analysis.";
        }

        if (topRootCauses.isEmpty()) {
            return "Incident history was analyzed, but no root cause pattern was available.";
        }

        String topRootCause =
                topRootCauses.getFirst();

        long matchingRootCauseCount =
                incidents.stream()
                        .map(IncidentSummary::getRootCause)
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(rootCause ->
                                topRootCause.equals(rootCause))
                        .count();

        long percentage =
                Math.round(
                        (matchingRootCauseCount * 100.0)
                                / incidents.size());

        return "%s related failures represent %d%% of incidents."
                .formatted(
                        topRootCause,
                        percentage);
    }

    private String buildHistoryRecommendation(
            List<String> topRootCauses,
            List<String> topOwnerTeams,
            String severityTrend) {

        String ownerTeam =
                topOwnerTeams.isEmpty()
                        ? "the responsible operations team"
                        : topOwnerTeams.getFirst();

        if (topRootCauses.isEmpty()) {
            return "Review incident classification and ensure root cause data is captured consistently.";
        }

        String rootCause =
                topRootCauses.getFirst();

        if ("HIGH_RISK".equals(severityTrend)) {
            return "Review %s capacity, timeout thresholds, and recovery controls with %s."
                    .formatted(
                            rootCause,
                            ownerTeam);
        }

        return "Monitor %s recurrence and review operational ownership with %s."
                .formatted(
                        rootCause,
                        ownerTeam);
    }
}
