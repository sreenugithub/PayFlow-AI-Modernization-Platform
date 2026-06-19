package com.payflow.api_gateway.util;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    private static final List<String> OPEN_ENDPOINTS = List.of(
            "/api/auth/register",
            "/api/auth/login",
            "/api/investigation",
            "/api/ai",
            "/actuator"
    );

    public Predicate<String> isSecured =
            path -> OPEN_ENDPOINTS.stream().noneMatch(path::startsWith);
}
