-- SQL Queries Covering Business Logic for GalleryApp

-- 1. CRUD Operations
-- Create a new image entry
INSERT INTO images (name, file_path) VALUES ('new_image.jpg', '/images/new_image.jpg');

-- Read image details by ID
SELECT * FROM images WHERE id = 1;

-- Update image name
UPDATE images SET name = 'updated_image.jpg' WHERE id = 1;

-- Delete image by ID
DELETE FROM images WHERE id = 1;

-- 2. Search Query with Dynamic Filters, Pagination, and Sorting
SELECT * FROM images
WHERE name ILIKE '%nature%'
ORDER BY created_at DESC
LIMIT 10 OFFSET 0;

-- 3. Search Query with Joined Data
-- Fetch images along with their metadata
SELECT i.id, i.name, i.file_path, m.location, m.width, m.height, m.created_at, m.modified_at
FROM images i
JOIN metadata m ON i.id = m.image_id;

-- 4. Statistic Query
-- Count of images per location
SELECT m.location, COUNT(i.id) AS image_count
FROM images i
JOIN metadata m ON i.id = m.image_id
GROUP BY m.location
ORDER BY image_count DESC;

-- 5. Use-Case Queries in Java Application
-- Fetch all images in a specific album
SELECT i.id, i.name, i.file_path
FROM images i
JOIN album_images ai ON i.id = ai.image_id
WHERE ai.album_id = 1;
