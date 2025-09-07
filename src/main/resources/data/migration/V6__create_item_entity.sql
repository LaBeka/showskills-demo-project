create table item_entities
(
    item_id    bigserial PRIMARY KEY NOT NULL,
    cart_id    bigserial NOT NULL,
    product_id bigserial not null,
    quantity   integer,
    foreign key (cart_id) references cart_entities (cart_id),
    foreign key (product_id) references product_entities (product_id)
);
