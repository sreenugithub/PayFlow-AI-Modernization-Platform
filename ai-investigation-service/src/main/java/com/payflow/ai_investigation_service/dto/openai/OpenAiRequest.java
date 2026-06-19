package com.payflow.ai_investigation_service.dto.openai;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenAiRequest {

    private String model;
    private String input;
}