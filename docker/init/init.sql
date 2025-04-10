--CREATE database IF NOT EXISTS gallery_db;
--USE gallery_db;

CREATE TABLE IF NOT EXISTS images (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    path VARCHAR(1024)
);

CREATE TABLE IF NOT EXISTS metadata (
    image_id INT PRIMARY KEY REFERENCES images(id) ON DELETE CASCADE,
    location TEXT,
    width INT,
    height INT,
    created_at TIMESTAMP,
    modified_at TIMESTAMP
);
