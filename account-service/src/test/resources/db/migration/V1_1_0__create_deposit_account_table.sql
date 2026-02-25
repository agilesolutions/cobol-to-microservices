CREATE TABLE if not exists deposit_account (
    id bigserial NOT NULL,
    account_number     varchar                          not null,
    account_type     varchar                         not null,
    balance     bigserial                           not null
);