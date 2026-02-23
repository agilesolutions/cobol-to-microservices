package com.agilesolutions.account.model;

import lombok.Builder;

@Builder
public record AccountResponse (String accountNumber, String accountType, double balance) {
}
