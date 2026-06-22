package com.example.paymentvalidation.service;

import com.example.paymentvalidation.client.AccountValidationClient;
import com.example.paymentvalidation.dto.AccountValidationResponse;
import com.example.paymentvalidation.dto.PaymentValidationRequest;
import com.example.paymentvalidation.dto.PaymentValidationResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentValidationServiceTest {

    @Test
    void shouldApprovePaymentWhenAccountIsValid() {
        AccountValidationClient client = Mockito.mock(AccountValidationClient.class);
        Mockito.when(client.validateAccount(Mockito.any()))
                .thenReturn(new AccountValidationResponse(true, "OK"));

        PaymentValidationService service = new PaymentValidationService(client);
        PaymentValidationResponse response = service.validatePayment(
                new PaymentValidationRequest("REF-1", "CARD", 100.0)
        );

        assertEquals("APPROVED", response.getStatus());
        assertTrue(response.isAccountValidated());
    }

    @Test
    void shouldRejectPaymentWhenAccountIsInvalid() {
        AccountValidationClient client = Mockito.mock(AccountValidationClient.class);
        Mockito.when(client.validateAccount(Mockito.any()))
                .thenReturn(new AccountValidationResponse(false, "Insufficient balance"));

        PaymentValidationService service = new PaymentValidationService(client);
        PaymentValidationResponse response = service.validatePayment(
                new PaymentValidationRequest("REF-2", "CARD", 50.0)
        );

        assertEquals("REJECTED", response.getStatus());
        assertFalse(response.isAccountValidated());
        assertEquals("Insufficient balance", response.getMessage());
    }
}
