package com.agilesolutions.customer.service;

import com.agilesolutions.customer.model.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LegacyCustomerService {

    private final WebClient webClient;

    public Mono<CustomerResponse> getCustomer(String id) {
        // Call z/OS Connect REST endpoint and forward the JWT token for authentication
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication())
                .cast(JwtAuthenticationToken.class)
                .flatMap(jwtAuth -> {
                    String token = jwtAuth.getToken().getTokenValue();

                    return webClient.get()
                            .uri("/legacy/customers/{id}", id)
                            .headers(h -> h.setBearerAuth(token))
                            .retrieve()
                            .bodyToMono(CustomerResponse.class);
                });

    }
}
