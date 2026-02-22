package com.agilesolutions.account.repository;

import com.agilesolutions.entity.DepositAccountEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DepositAccountRepository extends R2dbcRepository<DepositAccountEntity, Long> {

    @Override
    Mono<DepositAccountEntity> findById(Long aLong);

    Mono<DepositAccountEntity> findByAccountNumber(String accountNumber);

    @Override
    Flux<DepositAccountEntity> findAll();

    void deleteByAccountNumber(String accountNumber);
}