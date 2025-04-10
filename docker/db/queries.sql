-- Sample SQL Queries for GalleryApp

-- Insert sample data into images table
INSERT INTO images (name, path) VALUES ('sample1.jpg', '/images/sample1.jpg');
INSERT INTO images (name, path) VALUES ('sample2.jpg', '/images/sample2.jpg');

-- Insert metadata for images
INSERT INTO metadata (image_id, location, width, height, created_at, modified_at)
VALUES (1, 'New York', 1920, 1080, NOW(), NOW());

INSERT INTO metadata (image_id, location, width, height, created_at, modified_at)
VALUES (2, 'Los Angeles', 1280, 720, NOW(), NOW());

-- Create an album
INSERT INTO albums (name) VALUES ('Vacation Photos');

-- Associate images with an album
INSERT INTO album_images (album_id, image_id) VALUES (1, 1);
INSERT INTO album_images (album_id, image_id) VALUES (1, 2);


-- Fetch all images with metadata
SELECT i.id, i.name, i.path, m.location, m.width, m.height, m.created_at, m.modified_at
FROM images i
JOIN metadata m ON i.id = m.image_id;

-- Fetch images in an album
SELECT i.id, i.name, i.path
FROM images i
JOIN album_images ai ON i.id = ai.image_id
WHERE ai.album_id = 1;
