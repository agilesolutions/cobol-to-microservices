package com.agilesolutions.customer.service;

import com.agilesolutions.customer.model.CustomerResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class LegacyCustomerService {

    private final WebClient webClient;

    public LegacyCustomerService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://zosconnect-host:9080/zos").build();
    }

    public Mono<CustomerResponse> getCustomer(String id) {
        // Call z/OS Connect REST endpoint
        return webClient.get()
                .uri("/customers/{id}", id)
                .retrieve()
                .bodyToMono(CustomerResponse.class); // JSON mapped to Java class
    }
}
