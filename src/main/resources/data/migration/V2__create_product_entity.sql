create table product_entities
(
    product_id    bigserial PRIMARY KEY NOT NULL,
    name          varchar(128)          NOT NULL,
    description   text,
    initial_price float                 NOT NULL,
    storage_qty   integer               NOT NULL,
    discount      integer,
    current_price float NOT NULL,
    new_product   boolean,
    brand_name      varchar(128)          NOT NULL,
    category_name   varchar(128)          NOT NULL
);
