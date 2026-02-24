package com.agilesolutions.customer.controller;

import com.agilesolutions.customer.model.CustomerResponse;
import com.agilesolutions.customer.service.CustomerService;
import com.agilesolutions.customer.service.LegacyCustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(
        name = "CRUD REST APIs to CREATE, READ, UPDATE, DELETE customers",
        description = "CRUD REST APIs for managing customers"
)
@RestController
@AllArgsConstructor
@RequestMapping("/api/customers")
@Slf4j
public class CustomerController {

    private final LegacyCustomerService legacyCustomerService;

    private final CustomerService customerService;

    @Operation(
            summary = "Fetch all customers",
            description = "REST API to fetch all customer from the database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    }
    )
    @GetMapping("/fetch")
    public Mono<CustomerResponse> getLegacyCustomer(@RequestParam String id) {
        return legacyCustomerService.getCustomer(id);
    }

    @Operation
            (
                    summary = "Fetch all customers",
                    description = "Fetch all customers in the system"
            )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    }
    )
    @GetMapping("/fetchAll")
    public Flux<CustomerResponse> getAllDepositAccounts(@RequestParam String number) {
        log.info("Received request to fetch all accounts");
        return customerService.findCustomerByNumber(number);
    }
}
