create table user_entities
(
    user_id      bigserial PRIMARY KEY NOT NULL,
    full_name    varchar(128)          NOT NULL,
    account_name varchar(128)          NOT NULL,
    email        varchar(128)          NOT NULL,
    password     varchar(128)          NOT NULL,
    enabled      boolean
);
/*ctrl+alt+L*/