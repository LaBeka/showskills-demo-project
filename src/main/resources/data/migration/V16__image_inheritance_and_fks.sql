-- 1) Ensure required columns exist (some may already exist in your table)
ALTER TABLE image_entities
    ADD COLUMN IF NOT EXISTS image_type VARCHAR (16),
    ADD COLUMN IF NOT EXISTS user_id BIGINT,
    ADD COLUMN IF NOT EXISTS client_id BIGINT,
    ADD COLUMN IF NOT EXISTS product_id BIGINT,
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN IF NOT EXISTS content_type VARCHAR (128),
    ADD COLUMN IF NOT EXISTS object_key VARCHAR (512);

-- If you previously had a product_id column with wrong type/name, adjust here:
-- (Your snippet had productId mapped as numeric column name "product_id" already.)

-- 2) Backfill existing rows: treat legacy rows as PRODUCT images (adjust if needed)
UPDATE image_entities
SET image_type = COALESCE(image_type, 'PRODUCT')
WHERE image_type IS NULL;

-- 3) Add FKs (assumes your PKs are user_entities.user_id, client_entities.client_id, products.product_id)
ALTER TABLE image_entities
    ADD CONSTRAINT fk_image_product
        FOREIGN KEY (product_id) REFERENCES product_entities (product_id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_image_user
        FOREIGN KEY (user_id) REFERENCES user_entities(user_id) ON
DELETE
CASCADE,
    ADD CONSTRAINT fk_image_client
        FOREIGN KEY (client_id) REFERENCES client_entities(client_id) ON DELETE
CASCADE;

-- 4) Add a CHECK to enforce only the correct FK is set per discriminator
ALTER TABLE image_entities
    ADD CONSTRAINT ck_image_type_fk
        CHECK (
            (image_type = 'PRODUCT' AND product_id IS NOT NULL AND user_id IS NULL AND
             client_id IS NULL)
                OR (image_type = 'USER' AND user_id IS NOT NULL AND product_id IS NULL AND
                    client_id IS NULL)
                OR (image_type = 'CLIENT' AND client_id IS NOT NULL AND product_id IS NULL AND
                    user_id IS NULL)
            );

-- 5) Make discriminator NOT NULL
ALTER TABLE image_entities
    ALTER COLUMN image_type SET NOT NULL;

/*ctrl+alt+L*/