package com.agilesolutions.account.controller;

import com.agilesolutions.account.model.DepositAccount;
import com.agilesolutions.account.service.DepositAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(
        name = "CRUD REST APIs to CREATE, READ, UPDATE, DELETE accounts",
        description = "CRUD REST APIs for managing accounts"
)
@Slf4j
@RestController
@RequestMapping(path="/api/depositAccounts", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RequiredArgsConstructor
public class DepositAccountController {

    private final DepositAccountService depositAccountService;

    @Operation(
            summary = "Fetch single deposit accounts",
            description = "Fetch single deposit accounts by account id"
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
    public Mono<DepositAccount> getDepositAccount(@RequestParam String accountNumber) {
        log.info("Received request to fetch account with account number: {}", accountNumber);
        return depositAccountService.findDepositAccountByAccountNumber(accountNumber);
    }

    @Operation(
            summary = "Create a new deposit account",
            description = "Create a new deposit account with the provided details"
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
    @PostMapping("/create")
    public Mono<DepositAccount> createDepositAccount(@RequestBody DepositAccount depositAccount) {
        log.info("Received request to create account with account number: {}", depositAccount.accountNumber());
        return depositAccountService.save(depositAccount);
    }

    @Operation
    (
            summary = "Fetch all deposit accounts",
            description = "Fetch all deposit accounts in the system"
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
    public Flux<DepositAccount> getAllDepositAccounts() {
        log.info("Received request to fetch all accounts");
        return depositAccountService.findAllDepositAccounts();
    }

    @Operation
    (
            summary = "Delete a deposit account",
            description = "Delete a deposit account by account number"
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
    @DeleteMapping("/delete")
    public void deleteDepositAccount(@RequestParam String accountNumber) {
        log.info("Received request to delete account with account number: {}", accountNumber);
        depositAccountService.deleteByAccountNumber(accountNumber).subscribe();
    }
}
