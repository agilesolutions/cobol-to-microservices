package com.agilesolutions.account.controller;

import com.agilesolutions.account.service.DepositAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.Mockito.when;

class DepositAccountControllerTest {

    private WebTestClient webTestClient;

    private DepositAccountService depositAccountService;

    @BeforeEach
    void setUp() {
        depositAccountService = org.mockito.Mockito.mock(DepositAccountService.class);
        webTestClient = WebTestClient.bindToController(new DepositAccountController(depositAccountService)).build();
        webTestClient
                .mutate()
                .responseTimeout(java.time.Duration.ofSeconds(30))
                .build();
    }

    @Test
    void givenExistingAccount_whenFetchAccount_thenReturnAccount() {

        // Given
        when(depositAccountService.findDepositAccountByAccountNumber("1234567890")).thenReturn(
                reactor.core.publisher.Mono.just(
                        com.agilesolutions.account.model.DepositAccount.builder()
                                .id(1L)
                                .accountNumber("1234567890")
                                .accountType("SAVINGS")
                                .balance(1000.00)
                                .build()
                )
        );

        // When and Then
        webTestClient.get().uri("/api/depositAccounts/fetch?accountNumber=1234567890")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.accountNumber").isEqualTo("1234567890")
                .jsonPath("$.accountType").isEqualTo("SAVINGS")
                .jsonPath("$.balance").isEqualTo(1000.00);


    }

    @Test
    void givenNewAccount_whenSaveNewAccount_thenPersistAccount() {
            com.agilesolutions.account.model.DepositAccount newAccount = com.agilesolutions.account.model.DepositAccount.builder()
                    .accountNumber("0987654321")
                    .accountType("CHECKING")
                    .balance(500.00)
                    .build();

        // Given
        when(depositAccountService.save(newAccount)).thenReturn(
                reactor.core.publisher.Mono.just(
                        com.agilesolutions.account.model.DepositAccount.builder()
                                .id(2L)
                                .accountNumber("0987654321")
                                .accountType("CHECKING")
                                .balance(500.00)
                                .build()
                )
        );
        // When and Then
        webTestClient.post().uri("/api/depositAccounts/create")
                .bodyValue(newAccount)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.accountNumber").isEqualTo("0987654321")
                .jsonPath("$.accountType").isEqualTo("CHECKING")
                .jsonPath("$.balance").isEqualTo(500.00);


    }

    @Test
    void givenMultipleAccount_whenFetchingAccounts_returnAllAccounts() {

        //given
        when(depositAccountService.findAllDepositAccounts()).thenReturn(
                reactor.core.publisher.Flux.just(
                        com.agilesolutions.account.model.DepositAccount.builder()
                                .id(1L)
                                .accountNumber("1234567890")
                                .accountType("SAVINGS")
                                .balance(1000.00)
                                .build(),
                        com.agilesolutions.account.model.DepositAccount.builder()
                                .id(2L)
                                .accountNumber("0987654321")
                                .accountType("CHECKING")
                                .balance(500.00)
                                .build()
                )
        );

        //when and then
        webTestClient.get().uri("/api/depositAccounts/fetchAll")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].accountNumber").isEqualTo("1234567890")
                .jsonPath("$[0].accountType").isEqualTo("SAVINGS")
                .jsonPath("$[0].balance").isEqualTo(1000.00)
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].accountNumber").isEqualTo("0987654321")
                .jsonPath("$[1].accountType").isEqualTo("CHECKING")
                .jsonPath("$[1].balance").isEqualTo(500.00);



    }

    @Test
    void givenExistingAccount_whenDeleteAccount_thenRemoveAccount() {

        // Given
        when(depositAccountService.deleteByAccountNumber("1234567890")).thenReturn(
                reactor.core.publisher.Mono.empty()
        );

        // When and Then
        webTestClient.delete().uri("/api/depositAccounts/delete?accountNumber=1234567890")
                .exchange()
                .expectStatus().isOk();
    }
}