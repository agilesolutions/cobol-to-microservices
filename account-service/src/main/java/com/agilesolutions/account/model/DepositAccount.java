package com.agilesolutions.account.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DepositAccount(
        @NotEmpty(message = "Account ID can not be a null or empty")
        @Schema(
                description = "Unique id of account", example = "1"
        )
        Long id,
        @NotEmpty(message = "Account number can not be a null or empty")
        @Schema(
                description = "Account number", example = "1234567890"
        )
        String accountNumber,
        @NotEmpty(message = "Account type can not be a null or empty")
        @Schema(
                description = "Type of account", example = "SAVINGS"
        )
        String accountType,
        @NotEmpty(message = "Balance can not be a null or empty")
        @Schema(
                description = "Balance of account", example = "1000.00"
        )
        double balance) {
}
