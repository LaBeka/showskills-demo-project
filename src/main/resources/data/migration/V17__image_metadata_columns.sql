-- Add metadata columns used by ImageEntity
ALTER TABLE image_entities
    ADD COLUMN IF NOT EXISTS content_type VARCHAR (128),
    ADD COLUMN IF NOT EXISTS object_key VARCHAR (512),
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;

-- Backfill created_at for existing rows, then enforce NOT NULL + default going forward
UPDATE image_entities
SET created_at = COALESCE(created_at, now());

ALTER TABLE image_entities
    ALTER COLUMN created_at SET NOT NULL,
ALTER
COLUMN created_at SET DEFAULT now();
