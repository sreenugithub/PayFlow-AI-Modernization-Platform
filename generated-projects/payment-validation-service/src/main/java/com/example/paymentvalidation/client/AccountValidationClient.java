package com.example.paymentvalidation.client;

import com.example.paymentvalidation.dto.AccountValidationResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

@Component
public class AccountValidationClient {

    private final WebClient webClient;

    public AccountValidationClient(
            WebClient.Builder webClientBuilder,
            @Value("${external.account-validation.base-url:http://localhost:8081}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }
    public AccountValidationResponseDto validateAccount(
            String paymentReference,
            BigDecimal amount) {

        return AccountValidationResponseDto.builder()
                .accountActive(true)
                .sufficientFunds(true)
                .accountStatus("ACTIVE")
                .message("Mock account validation passed")
                .build();
    }
   /* public AccountValidationResponseDto validateAccount(String paymentReference, BigDecimal amount) {
        try {
            return webClient.post()
                    .uri("/api/accounts/validate")
                    .bodyValue(new AccountValidationRequest(paymentReference, amount))
                    .retrieve()
                    .bodyToMono(AccountValidationResponseDto.class)
                    .block();
        } catch (Exception ex) {
            return new AccountValidationResponseDto(false, false, "UNKNOWN", "Downstream account validation unavailable");
        }
    }
*/
    public record AccountValidationRequest(String paymentReference, BigDecimal amount) {}
}
