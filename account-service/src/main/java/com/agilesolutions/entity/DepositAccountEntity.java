package com.agilesolutions.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "deposit_account")
public class DepositAccountEntity extends BaseEntity {

    @Id
    @Column(value="id")
    private Long id;
    @Column(value="account_number")
    private String accountNumber;
    @Column(value="account_type")
    private String accountType;
    @Column(value="balance")
    private double balance;
}