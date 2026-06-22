package com.example.paymentvalidation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountValidationResponseDto {

    private boolean accountActive;
    private boolean sufficientFunds;
    private String accountStatus;
    private String message;
}
