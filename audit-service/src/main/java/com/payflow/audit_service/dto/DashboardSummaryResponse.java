package com.payflow.audit_service.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {

    private Long totalPayments;

    private Long healthyPayments;

    private Long warningPayments;

    private Long criticalPayments;
}

