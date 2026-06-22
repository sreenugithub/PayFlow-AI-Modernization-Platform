package com.example.paymentvalidation.controller;

import com.example.paymentvalidation.dto.PaymentValidationResponse;
import com.example.paymentvalidation.service.PaymentValidationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentValidationController.class)
class PaymentValidationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentValidationService paymentValidationService;

    @Test
    void shouldReturnBadRequestForInvalidPayload() throws Exception {
        mockMvc.perform(post("/api/payments/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"paymentReference\":\"\",\"paymentType\":\"CARD\",\"amount\":100.0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnOkForValidPayload() throws Exception {
        Mockito.when(paymentValidationService.validatePayment(Mockito.any()))
                .thenReturn(new PaymentValidationResponse("REF-1", "APPROVED", "Payment validated successfully", true));

        mockMvc.perform(post("/api/payments/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"paymentReference\":\"REF-1\",\"paymentType\":\"CARD\",\"amount\":100.0}"))
                .andExpect(status().isOk());
    }
}
