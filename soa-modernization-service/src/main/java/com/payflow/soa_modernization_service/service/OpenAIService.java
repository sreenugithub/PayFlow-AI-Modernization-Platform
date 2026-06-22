package com.payflow.soa_modernization_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAIService {

    private final WebClient.Builder webClientBuilder;

    @Value("${OPENAI_API_KEY:}")
    private String apiKey;

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    @Value("${openai.url:https://api.openai.com/v1/responses}")
    private String openAiUrl;

    public String generateProjectJson(String prompt) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "OPENAI_API_KEY is not configured for soa-modernization-service");
        }

        Map<String, Object> request = Map.of(
                "model", model,
                "input", prompt
        );

        Map response = webClientBuilder.build()
                .post()
                .uri(openAiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return extractText(response);
    }

    private String extractText(Map response) {

        if (response == null || response.get("output") == null) {
            return "";
        }

        List output = (List) response.get("output");

        if (output.isEmpty()) {
            return "";
        }

        Map firstOutput = (Map) output.get(0);

        List content = (List) firstOutput.get("content");

        if (content == null || content.isEmpty()) {
            return "";
        }

        Map firstContent = (Map) content.get(0);

        Object text = firstContent.get("text");

        return text == null ? "" : text.toString();
    }
}
