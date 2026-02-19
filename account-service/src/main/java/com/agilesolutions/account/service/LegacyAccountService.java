package com.agilesolutions.account.service;

import com.agilesolutions.account.model.AccountResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class LegacyAccountService {

    private final WebClient webClient;

    public LegacyAccountService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://zosconnect-host:9080/zos").build();
    }

    public Mono<AccountResponse> getAccount(String accountId) {
        // Call z/OS Connect REST endpoint
        return webClient.get()
                .uri("/accounts/{id}", accountId)
                .retrieve()
                .bodyToMono(AccountResponse.class); // JSON mapped to Java class
    }
}
