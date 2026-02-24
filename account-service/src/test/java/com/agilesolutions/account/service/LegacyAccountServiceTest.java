package com.agilesolutions.account.service;

import com.agilesolutions.account.model.AccountResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.ReactorContextTestExecutionListener;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@TestExecutionListeners({
        WithSecurityContextTestExecutionListener.class,
        ReactorContextTestExecutionListener.class // Crucial for Reactive support
})
class LegacyAccountServiceTest {

    private static WireMockServer wireMockServer;

    private WebClient webClient;

    private LegacyAccountService accountService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        accountService = new LegacyAccountService(WebClient.builder().baseUrl("http://localhost:" + wireMockServer.port()).build());
    }

    @Test
    @WithMockJwt(subject = "admin-user", roles = {"ADMIN"})
    void givenAccountId_whenGetAccount_thenReturnAccount() {
        // Given
        Long accountId = 1L;
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get("/legacy/accounts/1")
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