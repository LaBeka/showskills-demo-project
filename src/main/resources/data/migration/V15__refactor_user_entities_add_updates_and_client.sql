-- 1) Split full_name → first_name + last_name
ALTER TABLE user_entities
    ADD COLUMN first_name VARCHAR(128),
    ADD COLUMN last_name  VARCHAR(128);

-- Backfill: if full_name has space, split into first + last
UPDATE user_entities
SET first_name = split_part(full_name, ' ', 1),
    last_name  = btrim(substring(full_name FROM position(' ' IN full_name) + 1))
WHERE full_name LIKE '% %';

-- If no space, put into first_name, leave last_name empty
UPDATE user_entities
SET first_name = full_name,
    last_name  = ''
WHERE full_name IS NOT NULL
  AND full_name NOT LIKE '% %';

-- Enforce NOT NULL
ALTER TABLE user_entities
    ALTER COLUMN first_name SET NOT NULL,
ALTER
COLUMN last_name  SET NOT NULL;

-- Drop old full_name column
ALTER TABLE user_entities
DROP
COLUMN full_name;

-----------------------------------------------------------------

-- 2) Create user_updates table
CREATE TABLE user_updates
(
    id                 BIGSERIAL PRIMARY KEY,
    user_id            BIGINT    NOT NULL,
    updated_by_user_id BIGINT    NOT NULL,
    updated_at         TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT fk_user_updates_user
        FOREIGN KEY (user_id) REFERENCES user_entities (user_id),
    CONSTRAINT fk_user_updates_updated_by
        FOREIGN KEY (updated_by_user_id) REFERENCES user_entities (user_id)
);

-----------------------------------------------------------------

-- 3) Create client_entities table
CREATE TABLE client_entities
(
    client_id   BIGSERIAL PRIMARY KEY,
    client_name VARCHAR(128) NOT NULL
);

-----------------------------------------------------------------

-- 4) Add client_id FK in user_entities
ALTER TABLE user_entities
    ADD COLUMN client_id BIGINT;

ALTER TABLE user_entities
    ADD CONSTRAINT fk_user_entities_client
        FOREIGN KEY (client_id) REFERENCES client_entities (client_id);
