package com.agilesolutions.customer.controller;

import com.agilesolutions.customer.model.CustomerResponse;
import com.agilesolutions.customer.service.LegacyCustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Tag(
        name = "CRUD REST APIs to CREATE, READ, UPDATE, DELETE customers",
        description = "CRUD REST APIs for managing customers"
)
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CustomerController {

    private final LegacyCustomerService legacyCustomerService;

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
    @GetMapping("/customers/{id}")
    public Mono<CustomerResponse> getLegacyCustomer(@PathVariable String id) {
        return legacyCustomerService.getCustomer(id);
    }
}
