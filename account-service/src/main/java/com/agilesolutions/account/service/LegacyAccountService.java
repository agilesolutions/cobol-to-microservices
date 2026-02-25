package com.agilesolutions.account.service;

import com.agilesolutions.account.model.AccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LegacyAccountService {

    private final WebClient webClient;

    public Mono<AccountResponse> getAccount(String accountId) {
        // Call z/OS Connect REST endpoint end forward the JWT token for authentication
        Mono<AccountResponse> response = ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication())
                .cast(JwtAuthenticationToken.class)
                .flatMap(jwtAuth -> {
                    String token = jwtAuth.getToken().getTokenValue();

                    return webClient.get()
                            .uri("/legacy/accounts/{id}", accountId)
                            .headers(h -> h.setBearerAuth(token))
                            .retrieve()
                            .bodyToMono(AccountResponse.class);
                });

        return response;

    }
}
