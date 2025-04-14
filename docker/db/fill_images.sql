-- fill_images.sql
DO $$
BEGIN
  FOR i IN 1..1000000 LOOP
    INSERT INTO images (name, path)
    VALUES (
      'image_' || i,
      '/images/image_' || i || '.jpg'
    );
  END LOOP;
END $$;
