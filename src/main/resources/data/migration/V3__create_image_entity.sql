create table image_entities
(
    image_id   bigserial PRIMARY KEY NOT NULL,
    product_id bigserial             NOT NULL,
    image_name varchar(220),
    foreign key (product_id) references product_entities (product_id)
);
