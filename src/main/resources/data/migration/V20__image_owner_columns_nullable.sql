-- All owner columns must be nullable in SINGLE_TABLE polymorphism
ALTER TABLE image_entities
    ALTER COLUMN product_id DROP NOT NULL,
ALTER COLUMN user_id    DROP NOT NULL,
  ALTER COLUMN client_id  DROP NOT NULL;

-- Also remove any accidental defaults (safety)
ALTER TABLE image_entities
    ALTER COLUMN product_id DROP DEFAULT,
ALTER COLUMN user_id    DROP DEFAULT,
  ALTER COLUMN client_id  DROP DEFAULT;


                               -- Column nullability
SELECT column_name, is_nullable
FROM information_schema.columns
WHERE table_name = 'image_entities'
  AND column_name IN ('product_id','user_id','client_id');

-- Check the NOT NULL constraint is gone
-- \d image_entities
