package com.agilesolutions.account.controller;

import com.agilesolutions.account.service.LegacyAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.Mockito.when;

class AccountControllerTest {

    private WebTestClient webTestClient;

    private LegacyAccountService legacyAccountService;

    @BeforeEach
    void setUp() {
        legacyAccountService = org.mockito.Mockito.mock(LegacyAccountService.class);
        webTestClient = WebTestClient.bindToController(new AccountController(legacyAccountService)).build();
        webTestClient
                .mutate()
                .responseTimeout(java.time.Duration.ofSeconds(30))
                .build();
    }

    /**
     * Tests that the endpoint returns an account as JSON when the account exists.
     * Verifies the HTTP status is 200 (OK) and the response contains the expected data.
     *
     * @throws Exception if the MockMvc request fails
     */
    @Test
    @DisplayName("Returns account as JSON when account exist")
    void givenAccountExist_whenFetchingAccount_thenReturnAccount() {

        // Given
        when(legacyAccountService.getAccount("1")).thenReturn(
                reactor.core.publisher.Mono.just(
                        com.agilesolutions.account.model.AccountResponse.builder()
                                .accountNumber("1234567890")
                                .accountType("SAVINGS")
                                .balance(1000.00)
                                .build()
                )
        );

        // When and Then
        webTestClient.get().uri("/api/accounts/fetch?id=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.accountNumber").isEqualTo("1234567890")
                .jsonPath("$.accountType").isEqualTo("SAVINGS")
                .jsonPath("$.balance").isEqualTo(1000.00);

    }

}