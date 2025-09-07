-- 1) Remove any accidental defaults (safety)
ALTER TABLE image_entities
    ALTER COLUMN product_id DROP DEFAULT,
ALTER
COLUMN user_id    DROP
DEFAULT,
  ALTER
COLUMN client_id  DROP
DEFAULT;

-- 2) Normalize existing data so exactly one owner matches image_type
UPDATE image_entities
SET user_id   = NULL,
    client_id = NULL
WHERE image_type = 'PRODUCT';

UPDATE image_entities
SET product_id = NULL,
    client_id  = NULL
WHERE image_type = 'USER';

UPDATE image_entities
SET product_id = NULL,
    user_id    = NULL
WHERE image_type = 'CLIENT';