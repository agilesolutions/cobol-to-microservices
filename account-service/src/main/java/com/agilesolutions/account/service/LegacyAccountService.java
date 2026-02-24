package com.agilesolutions.account.service;

import com.agilesolutions.account.model.AccountResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LegacyAccountService {

    private final WebClient webClient;

    public Mono<AccountResponse> getAccount(String accountId) {
        Mono<SecurityContext> context = ReactiveSecurityContextHolder.getContext();

//        SecurityContext mycontext = context.block();

        // Call z/OS Connect REST endpoint end forward the JWT token for authentication
        Mono<AccountResponse> response = context
                .doOnNext(ctx -> {

                    System.out.println("SecurityContext: " + ctx.getAuthentication().getName());


                    System.out.println("SecurityContext: " + ctx);

                })
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
