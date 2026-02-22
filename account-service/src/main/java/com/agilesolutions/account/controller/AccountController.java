package com.agilesolutions.account.controller;

import com.agilesolutions.account.model.AccountResponse;
import com.agilesolutions.account.service.LegacyAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(
        name = "CRUD REST APIs to CREATE, READ, UPDATE, DELETE accounts",
        description = "CRUD REST APIs for managing accounts"
)
@Slf4j
@RestController
@RequestMapping(path = "/api/accounts", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AccountController {

    private final LegacyAccountService legacyAccountService;

    public AccountController(LegacyAccountService legacyAccountService) {
        this.legacyAccountService = legacyAccountService;
    }

    @Operation(
            summary = "Fetch all accounts",
            description = "REST API to fetch all accounts from the database"
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
    public Mono<AccountResponse> getLegacyAccount(@RequestParam String id) {
        log.info("Received request to fetch account with id: {}", id);
        return legacyAccountService.getAccount(id);
    }

}
