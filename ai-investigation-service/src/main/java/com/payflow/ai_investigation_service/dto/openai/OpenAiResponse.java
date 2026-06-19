package com.payflow.ai_investigation_service.dto.openai;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiResponse {

    private List<Output> output;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Output {
        private List<Content> content;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content {
        private String text;
    }
}