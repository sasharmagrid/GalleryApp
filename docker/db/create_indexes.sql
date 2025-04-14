-- create_indexes.sql

-- Index on 'name' for faster lookup by name
CREATE INDEX IF NOT EXISTS idx_images_name ON images(name);

-- Compound index: useful for queries filtering by both name and path
CREATE INDEX IF NOT EXISTS idx_images_name_path ON images(name, path);
