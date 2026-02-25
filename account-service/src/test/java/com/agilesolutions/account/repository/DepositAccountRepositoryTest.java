package com.agilesolutions.account.repository;

import com.agilesolutions.entity.DepositAccountEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataR2dbcTest
@Slf4j
// Ensure Flyway is auto-configured even in a slice test
@ImportAutoConfiguration(FlywayAutoConfiguration.class)
class DepositAccountRepositoryTest {

    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    R2dbcEntityTemplate template;

    @Autowired
    private DepositAccountRepository repository;


    @Test
    void giveDepositAccountEntity_whenFindById_thenReturnDepositAccountEntity() {

        this.template.insert(new DepositAccountEntity(1L, "1234567890", "SAVINGS", 1000.00))
                .flatMap(client ->
                        repository.findById(client.getId())
                )
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(account -> {
                            log.info("saved account: {}", account);
                            assertThat(account.getAccountNumber()).isEqualTo("1234567890");
                            assertThat(account.getAccountType()).isEqualTo("SAVINGS");
                            assertThat(account.getBalance()).isEqualTo(1000.00);
                        }
                )
                .verifyComplete();


    }

    @Test
    void giveDepositAccountEntity_whenFindByAccountNumber_thenReturnDepositAccountEntity() {

        this.template.insert(new DepositAccountEntity(2L, "0987654321", "CHECKING", 500.00))
                .flatMap(client ->
                        repository.findByAccountNumber(client.getAccountNumber())
                )
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(account -> {
                            log.info("saved account: {}", account);
                            assertThat(account.getAccountNumber()).isEqualTo("0987654321");
                            assertThat(account.getAccountType()).isEqualTo("CHECKING");
                            assertThat(account.getBalance()).isEqualTo(500.00);
                        }
                )
                .verifyComplete();
    }

    @Test
    void giveDepositAccountEntity_whenFindAll_thenReturnFluxOfDepositAccountEntity() {

        this.template.insert(new DepositAccountEntity(3L, "1111111111", "SAVINGS", 2000.00))
                .then(this.template.insert(new DepositAccountEntity(4L, "2222222222", "CHECKING", 1500.00)))
                .thenMany(repository.findAll())
                .log()
                .as(StepVerifier::create)
                .expectNextCount(4) // Assuming there are already 2 accounts in the database from previous tests
                .verifyComplete();
    }

    @Test
    void giveDepositAccountEntity_whenDeleteByAccountNumber_thenDeleteDepositAccountEntity() {

        this.template.insert(new DepositAccountEntity(5L, "3333333333", "SAVINGS", 3000.00))
                .then(repository.deleteByAccountNumber("3333333333"))
                .then(repository.findByAccountNumber("3333333333"))
                .log()
                .as(StepVerifier::create)
                .expectNextCount(0) // Expect no account to be found after deletion
                .verifyComplete();
    }
}