package com.agilesolutions.account.service;

import com.agilesolutions.account.repository.DepositAccountRepository;
import com.agilesolutions.entity.DepositAccountEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DepositAccountServiceTest {

    @Mock
    private DepositAccountRepository depositAccountRepository;

    @InjectMocks
    private DepositAccountService depositAccountService;

    /**
     * Initializes mocks before each test execution.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("Given accounts, when findAllDepositAccounts, then return all accounts")
    void givenAccounts_whenFindAllDepositAccount_thenReturnAllAccounts() {

        // given
        when(depositAccountRepository.findAll()).thenReturn(Flux.just(
                DepositAccountEntity.builder()
                        .id(1L)
                        .accountNumber("123456789")
                        .accountType("SAVINGS")
                        .balance(1000.0)
                        .build(),
                DepositAccountEntity.builder()
                        .id(2L)
                        .accountNumber("987654321")
                        .accountType("CHECKING")
                        .balance(2000.0)
                        .build()));

        // when
        Flux<com.agilesolutions.account.model.DepositAccount> depositAccountFlux = depositAccountService.findAllDepositAccounts();

        // then
        depositAccountFlux.collectList().blockOptional().ifPresent(depositAccounts -> {
            assertEquals(2, depositAccounts.size());
            assertEquals("123456789", depositAccounts.get(0).accountNumber());
            assertEquals("SAVINGS", depositAccounts.get(0).accountType());
            assertEquals(1000.0, depositAccounts.get(0).balance());
            assertEquals("987654321", depositAccounts.get(1).accountNumber());
            assertEquals("CHECKING", depositAccounts.get(1).accountType());
            assertEquals(2000.0, depositAccounts.get(1).balance());
        });

    }

    @Test
    @DisplayName("Given account number, when findDepositAccountByAccountNumber, then return account")
    void givenAccountNumber_whenFindDepositAccountByAccountNumber_thenReturnAccount() {

        // given
            String accountNumber = "123456789";
            when(depositAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Mono.just(
                    DepositAccountEntity.builder()
                            .id(1L)
                            .accountNumber(accountNumber)
                            .accountType("SAVINGS")
                            .balance(1000.0)
                            .build()));

            // when
            com.agilesolutions.account.model.DepositAccount depositAccount = depositAccountService.findDepositAccountByAccountNumber(accountNumber).block();

            // then
            assertNotNull(depositAccount);
            assertEquals(accountNumber, depositAccount.accountNumber());
            assertEquals("SAVINGS", depositAccount.accountType());
            assertEquals(1000.0, depositAccount.balance());


    }

    @Test
    @DisplayName("Given deposit account, when save, then return saved account")
    void givenDepositAccount_whenSave_thenReturnSavedAccount() {

        // given
        com.agilesolutions.account.model.DepositAccount depositAccountToSave = com.agilesolutions.account.model.DepositAccount.builder()
                .accountNumber("123456789")
                .accountType("SAVINGS")
                .balance(1000.0)
                .build();

        when(depositAccountRepository.save(DepositAccountEntity.builder()
                        .accountNumber(depositAccountToSave.accountNumber())
                        .accountType(depositAccountToSave.accountType())
                        .balance(depositAccountToSave.balance())
                        .build()))
                .thenReturn(Mono.just(
                        DepositAccountEntity.builder()
                                .id(1L)
                                .accountNumber(depositAccountToSave.accountNumber())
                                .accountType(depositAccountToSave.accountType())
                                .balance(depositAccountToSave.balance())
                                .build()));

        // when
        com.agilesolutions.account.model.DepositAccount savedDepositAccount = depositAccountService.save(depositAccountToSave).block();

        // then
        assertNotNull(savedDepositAccount);
        assertEquals(1L, savedDepositAccount.id());
        assertEquals("123456789", savedDepositAccount.accountNumber());
        assertEquals("SAVINGS", savedDepositAccount.accountType());
        assertEquals(1000.0, savedDepositAccount.balance());
    }

    @Test
    @DisplayName("Given account number, when deleteByAccountNumber, then delete account")
    void givenAccountNumber_whenDeleteByAccountNumber_thenDeleteAccount() {

        // given
        String accountNumber = "123456789";

        // when
        Mono<Void> deleteResult = depositAccountService.deleteByAccountNumber(accountNumber);

        // then
        assertNotNull(deleteResult);
    }
}