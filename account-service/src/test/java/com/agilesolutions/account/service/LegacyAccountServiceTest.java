package com.agilesolutions.account.service;

import com.agilesolutions.account.model.AccountResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class LegacyAccountServiceTest {

    private static WireMockServer wireMockServer;

    private WebTestClient webTestClient;

    private LegacyAccountService accountService;

    private WebClient.Builder webClientBuilder = WebClient.builder();

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();

        webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + wireMockServer.port()).build();
        accountService = new LegacyAccountService(
                WebClient.builder().baseUrl("http://localhost:" + wireMockServer.port())
        );
    }

    @Test
    void givenAccountId_whenGetAccount_thenReturnAccount() {
        // Given
        Long accountId = 1L;
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get("/accounts/1")
                .willReturn(com.github.tomakehurst.wiremock.client.WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "id": 1,
                                    "clientId": 1,
                                    "accountNumber": "123456789",
                                    "balance": 1000.0
                                }
                                """)));

        // When
        Mono<AccountResponse> accountResponseMono = accountService.getAccount("1");

        // Then
        StepVerifier.create(accountResponseMono)
                .expectNextMatches(accountResponse ->
                        accountResponse.accountNumber().equals("123456789") &&
                        accountResponse.balance() == 1000.0
                )
                .verifyComplete();
    }
}