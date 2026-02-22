package com.agilesolutions.customer.service;

import com.agilesolutions.customer.model.CustomerResponse;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class LegacyCustomerService {

    private final WebClient webClient;

    public LegacyCustomerService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://gateway-service").build();
    }

    public Mono<CustomerResponse> getCustomer(String id) {
        // Call z/OS Connect REST endpoint
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
