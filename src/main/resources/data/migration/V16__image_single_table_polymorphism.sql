-- add discriminator + FK columns to base table
ALTER TABLE image_entities
    ADD COLUMN IF NOT EXISTS image_type VARCHAR (16),
    ADD COLUMN IF NOT EXISTS product_id BIGINT,
    ADD COLUMN IF NOT EXISTS user_id BIGINT,
    ADD COLUMN IF NOT EXISTS client_id BIGINT;

ALTER TABLE image_entities
DROP
CONSTRAINT IF EXISTS fk_img_product;
ALTER TABLE image_entities
    ADD CONSTRAINT fk_img_product FOREIGN KEY (product_id)
        REFERENCES product_entities (product_id);

ALTER TABLE image_entities
DROP
CONSTRAINT IF EXISTS fk_img_user;
ALTER TABLE image_entities
    ADD CONSTRAINT fk_img_user FOREIGN KEY (user_id)
        REFERENCES user_entities (user_id);

ALTER TABLE image_entities
DROP
CONSTRAINT IF EXISTS fk_img_client;
ALTER TABLE image_entities
    ADD CONSTRAINT fk_img_client FOREIGN KEY (client_id)
        REFERENCES client_entities (client_id);

-- Optional: keep model & data consistent with a CHECK constraint
ALTER TABLE image_entities
    ADD CONSTRAINT ck_img_type_owner CHECK (
        (image_type = 'PRODUCT' AND product_id IS NOT NULL AND user_id IS NULL AND
         client_id IS NULL)
            OR
        (image_type = 'USER' AND user_id IS NOT NULL AND product_id IS NULL AND client_id IS NULL)
            OR
        (image_type = 'CLIENT' AND client_id IS NOT NULL AND product_id IS NULL AND user_id IS NULL)
        );

UPDATE image_entities
SET image_type = 'PRODUCT'
WHERE image_type IS NULL
  AND product_id IS NOT NULL;