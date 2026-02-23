package com.agilesolutions.account.service;

import com.agilesolutions.account.model.DepositAccount;
import com.agilesolutions.account.repository.DepositAccountRepository;
import com.agilesolutions.entity.DepositAccountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DepositAccountService {

    private final DepositAccountRepository depositAccountRepository;

    public Flux<DepositAccount> findAllDepositAccounts() {
        return depositAccountRepository.findAll()
                .map(depositAccount -> DepositAccount.builder()
                        .id(depositAccount.getId())
                        .accountNumber(depositAccount.getAccountNumber())
                        .accountType(depositAccount.getAccountType())
                        .balance(depositAccount.getBalance())
                        .build());
    }

    public Mono<DepositAccount> findDepositAccountByAccountNumber(String accountNumber) {
        return depositAccountRepository.findByAccountNumber(accountNumber)
                .map(depositAccount -> DepositAccount.builder()
                        .id(depositAccount.getId())
                        .accountNumber(depositAccount.getAccountNumber())
                        .accountType(depositAccount.getAccountType())
                        .balance(depositAccount.getBalance())
                        .build());
    }

    public Mono<DepositAccount> save(DepositAccount depositAccount) {
        return depositAccountRepository.save(DepositAccountEntity.builder()
                        .accountNumber(depositAccount.accountNumber())
                        .accountType(depositAccount.accountType())
                        .balance(depositAccount.balance())
                        .build())
                .map(savedDepositAccount -> DepositAccount.builder()
                        .id(savedDepositAccount.getId())
                        .accountNumber(savedDepositAccount.getAccountNumber())
                        .accountType(savedDepositAccount.getAccountType())
                        .balance(savedDepositAccount.getBalance())
                        .build());
    }

    public Mono<Void> deleteByAccountNumber(String accountNumber) {
        return Mono.fromRunnable(() -> depositAccountRepository.deleteByAccountNumber(accountNumber));
    }

}
