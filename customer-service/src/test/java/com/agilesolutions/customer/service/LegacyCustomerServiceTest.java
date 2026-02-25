package com.agilesolutions.customer.service;

import com.agilesolutions.customer.model.CustomerResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.test.context.support.ReactorContextTestExecutionListener;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@TestExecutionListeners({
        WithSecurityContextTestExecutionListener.class,
        ReactorContextTestExecutionListener.class // Crucial for Reactive support
})
class LegacyCustomerServiceTest {

    private static WireMockServer wireMockServer;

    private WebClient webClient;

    private LegacyCustomerService customerService;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        customerService = new LegacyCustomerService(WebClient.builder().baseUrl("http://localhost:" + wireMockServer.port()).build());
    }

    @Test
    @WithMockJwt(subject = "admin-user", roles = {"ADMIN"})
    void givenCustomerId_whenGetCustomer_thenReturnCustomer() {

        // Given
        Long customerId = 1L;
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get("/legacy/customers/1")
                .willReturn(com.github.tomakehurst.wiremock.client.WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "customerNumber": "CUST12345",
                                    "customerType": "INDIVIDUAL",
                                    "customerName": "John Doe"
                                }
                                """)));

        // When
        Mono<CustomerResponse> customerResponseMono = customerService.getCustomer("1");

        // Then
        reactor.test.StepVerifier.create(customerResponseMono)
                .expectNextMatches(customerResponse ->
                        customerResponse.customerNumber().equals("CUST12345") &&
                        customerResponse.customerType().equals("INDIVIDUAL") &&
                        customerResponse.customerName().equals("John Doe")
                )
                .verifyComplete();
                                    
                                    
                                      
    }
}