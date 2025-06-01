package com.programmingtechie.api_gateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmingtechie.api_gateway.dto.response.ApiResponse;
import com.programmingtechie.api_gateway.service.IdentityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {
    IdentityService identityService;
    ObjectMapper objectMapper;

    @NonFinal
    private String[] publicEndpoints = {
        "/identity/.*",

        // Doctor-Service
        "/doctor/public(/.*)?",
        "/doctor/qualification/public(/.*)?",
        "/doctor/specialty/public(/.*)?",

        // Medical-Service
        "/medical/service-time-frame/public(/.*)?",
        "/medical/service-type/public(/.*)?",
        "/medical/service/public(/.*)?",
        "/medical/doctor-service/public(/.*)?",
        "/medical/service-time-frame/public(/.*)?",
        "/medical/doctor-service/public(/.*)?",
        "/medical/time-frame/public(/.*)?",

        // Appointment Service
        "/appointment/public(/.*)?",
        "/appointment/payment/public(/.*)?",
        "/appointment/health-check-result/public(/.*)?",

        "/patient/public(/.*)?"
    };

    @Value("${app.api-prefix}")
    @NonFinal
    private String apiPrefix;

    private boolean isPublicEndpoint(ServerHttpRequest request) {
        return Arrays.stream(publicEndpoints)
                .anyMatch(s -> request.getURI().getPath().matches(apiPrefix + s));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Enter authentication filter....");

        if (isPublicEndpoint(exchange.getRequest())) return chain.filter(exchange);

        // Get token from authorization header
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeader)) {
            return unauthenticated(exchange.getResponse());
        }

        String token = authHeader.get(0).replace("Bearer ", "");
        log.info("Token: {}", token);

        identityService.introspect(token).subscribe(intro -> {
            log.info("Result: {}", intro.isValid());
        });

        return identityService
                .introspect(token)
                .flatMap(introspectResponse -> {
                    if (introspectResponse.isValid()) {
                        return chain.filter(exchange);
                    } else {
                        return unauthenticated(exchange.getResponse());
                    }
                })
                .onErrorResume(e -> {
                    log.error("Error during introspection", e);
                    return unauthenticated(exchange.getResponse());
                });
    }

    @Override
    public int getOrder() {
        return -1;
    }

    Mono<Void> unauthenticated(ServerHttpResponse response) {
        ApiResponse<?> apiResponse =
                ApiResponse.builder().code(1401).message("Unauthenticated").build();

        String body = null;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        log.info("Da xac nhan...");
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}
