package com.example.paymentvalidation.client;

import com.example.paymentvalidation.dto.AccountValidationRequest;
import com.example.paymentvalidation.dto.AccountValidationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AccountValidationClient {

    private final WebClient webClient;

    public AccountValidationClient(WebClient.Builder builder,
                                   @Value("${external.account-validation.base-url:http://localhost:8081}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    public AccountValidationResponse validateAccount(AccountValidationRequest request) {
        try {
            return webClient.post()
                    .uri("/api/accounts/validate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(AccountValidationResponse.class)
                    .block();
        } catch (Exception ex) {
            return new AccountValidationResponse(false, "Account validation service unavailable");
        }
    }
}
