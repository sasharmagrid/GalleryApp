--CREATE database IF NOT EXISTS gallery_db;
--USE gallery_db;

-- Database schema for GalleryApp

CREATE TABLE IF NOT EXISTS images (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    file_path TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS metadata (
    image_id INT PRIMARY KEY,
    location VARCHAR(255),
    width INT,
    height INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (image_id) REFERENCES images(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS albums (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS album_images (
    album_id INT,
    image_id INT,
    PRIMARY KEY (album_id, image_id),
    FOREIGN KEY (album_id) REFERENCES albums(id) ON DELETE CASCADE,
    FOREIGN KEY (image_id) REFERENCES images(id) ON DELETE CASCADE
);
