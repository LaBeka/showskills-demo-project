create table cart_entities
(
    cart_id    bigserial PRIMARY KEY NOT NULL,
    user_id    bigint not null,
    foreign key (user_id) references user_entities (user_id)
);
