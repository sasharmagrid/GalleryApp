package org.example.utils;

import org.example.models.Image;
import org.example.models.Metadata;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    private static final String IMAGE_DATA_FILE = "/Users/sasharma/Documents/GalleryApp/src/main/java/data/images.txt";
    private static final String METADATA_FILE = "/Users/sasharma/Documents/GalleryApp/src/main/java/data/metadata.txt";

    public static List<Image> loadImages() {
        List<Image> images = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(IMAGE_DATA_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                images.add(new Image(parts[0], parts[1], parts[2]));
            }
        } catch (IOException e) {
            System.out.println("Error loading metadata");
        }
        return images;
    }

    public static Metadata loadMetadata(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader(METADATA_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(id)) {
                    return new Metadata(parts[0], parts[1], parts[2],
                            Integer.parseInt(parts[3]), Integer.parseInt(parts[4]),
                            LocalDateTime.parse(parts[5]), LocalDateTime.parse(parts[6]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading metadata for ID: " + id);
        }
        return null;
    }

    public static void updateMetadata(String id, String name, String location) {
        List<Metadata> metadataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(METADATA_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(id)) {
                    metadataList.add(new Metadata(id, name, location, Integer.parseInt(parts[3]), Integer.parseInt(parts[4]),
                            LocalDateTime.parse(parts[5]), LocalDateTime.parse(parts[6])));
                } else {
                    metadataList.add(new Metadata(parts[0], parts[1], parts[2],
                            Integer.parseInt(parts[3]), Integer.parseInt(parts[4]),
                            LocalDateTime.parse(parts[5]), LocalDateTime.parse(parts[6])));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading metadata for ID: " + id);
            return;
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(METADATA_FILE))) {
            for (Metadata md : metadataList) {
                bw.write(md.getId() + "," + md.getName() + "," + md.getLocation() + "," + md.getWidth() + "," + md.getHeight() +
                        "," + md.getCreatedDateTime() + "," + md.getModifiedDateTime() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error loading metadata for ID: " + id);
        }
    }

    // Add image to images.txt
    public static void addImage(Image image) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(IMAGE_DATA_FILE, true))) {
            bw.write(image.getId() + "," + image.getName() + "," + image.getFilePath() + "\n");
        } catch (IOException e) {
            System.out.println("Error loading metadata");
        }
    }

    // Add metadata to metadata.txt
    public static void addMetadata(Metadata metadata) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(METADATA_FILE, true))) {
            bw.write(metadata.getId() + "," + metadata.getName() + "," + metadata.getLocation() + "," + metadata.getWidth() +
                    "," + metadata.getHeight() + "," + metadata.getCreatedDateTime() + "," + metadata.getModifiedDateTime() + "\n");
        } catch (IOException e) {
            System.out.println("Error loading metadata");
        }
    }

    // Method to extract image's dimensions and timestamps
    public static Metadata extractMetadata(String imagePath) {
        try {
            // Read image for dimensions
            BufferedImage img = ImageIO.read(new File(imagePath));
            if(img==null) return null;
            int width = img.getWidth();
            int height = img.getHeight();

            // Get file metadata for created and modified times
            Path path = Paths.get(imagePath);
            LocalDateTime createdDateTime = LocalDateTime.ofInstant(Files.readAttributes(path, BasicFileAttributes.class)
                    .creationTime().toInstant(), java.time.ZoneId.systemDefault());
            LocalDateTime modifiedDateTime = LocalDateTime.ofInstant(Files.readAttributes(path, BasicFileAttributes.class)
                    .lastModifiedTime().toInstant(), java.time.ZoneId.systemDefault());

            // Return new Metadata object
            return new Metadata("0", new File(imagePath).getName(), imagePath, width, height, createdDateTime, modifiedDateTime);
        } catch (IOException e) {
            System.out.println("Error loading metadata");
            return null;
        }
    }

    public static void removeImage(String id) {
        List<Image> images = loadImages();
        images.removeIf(img -> img.getId().equals(id));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(IMAGE_DATA_FILE))) {
            for (Image img : images) {
                bw.write(img.getId() + "," + img.getName() + "," + img.getFilePath() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error loading metadata");
        }
    }

    public static void removeMetadata(String id) {
        List<Metadata> metadataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(METADATA_FILE))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (!parts[0].equals(id)) { // Keep only metadata not matching the deleted image ID
                    metadataList.add(new Metadata(parts[0], parts[1], parts[2],
                            Integer.parseInt(parts[3]), Integer.parseInt(parts[4]),
                            LocalDateTime.parse(parts[5]), LocalDateTime.parse(parts[6])));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading metadata for ID: " + id);
        }

        // Rewrite metadata file without the deleted entry
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(METADATA_FILE))) {
            for (Metadata md : metadataList) {
                bw.write(md.getId() + "," + md.getName() + "," + md.getLocation() + ","
                        + md.getWidth() + "," + md.getHeight() + ","
                        + md.getCreatedDateTime() + "," + md.getModifiedDateTime() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error loading metadata for ID: " + id);
        }
    }

    public static void updateImageName(String id, String newName, String newPath) {
        List<Image> images = loadImages();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(IMAGE_DATA_FILE))) {
            for (Image img : images) {
                if (img.getId().equals(id)) {
                    bw.write(id + "," + newName + "," + newPath + "\n");
                } else {
                    bw.write(img.getId() + "," + img.getName() + "," + img.getFilePath() + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading metadata for ID: " + id);
        }
    }

    public static void updateImageLocation(String id, String newLocation) {
        List<Image> images = loadImages();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(IMAGE_DATA_FILE))) {
            for (Image img : images) {
                if (img.getId().equals(id)) {
                    bw.write(id + "," + img.getName() + "," + newLocation + "\n");
                } else {
                    bw.write(img.getId() + "," + img.getName() + "," + img.getFilePath() + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading metadata for ID: " + id);
        }
    }

    public static void updateMetadataLocation(String id, String newLocation) {
        List<Metadata> metadataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(METADATA_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(id)) {
                    metadataList.add(new Metadata(parts[0], parts[1], newLocation,
                            Integer.parseInt(parts[3]), Integer.parseInt(parts[4]),
                            LocalDateTime.parse(parts[5]), LocalDateTime.parse(parts[6])));
                } else {
                    metadataList.add(new Metadata(parts[0], parts[1], parts[2],
                            Integer.parseInt(parts[3]), Integer.parseInt(parts[4]),
                            LocalDateTime.parse(parts[5]), LocalDateTime.parse(parts[6])));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading metadata for ID: " + id);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(METADATA_FILE))) {
            for (Metadata md : metadataList) {
                bw.write(md.getId() + "," + md.getName() + "," + md.getLocation() + ","
                        + md.getWidth() + "," + md.getHeight() + ","
                        + md.getCreatedDateTime() + "," + md.getModifiedDateTime() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error loading metadata for ID: " + id);
        }
    }

    public static void updateMetadataName(String id, String newName) {
        List<Metadata> metadataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(METADATA_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(id)) {
                    metadataList.add(new Metadata(parts[0], newName, parts[2],
                            Integer.parseInt(parts[3]), Integer.parseInt(parts[4]),
                            LocalDateTime.parse(parts[5]), LocalDateTime.parse(parts[6])));
                } else {
                    metadataList.add(new Metadata(parts[0], parts[1], parts[2],
                            Integer.parseInt(parts[3]), Integer.parseInt(parts[4]),
                            LocalDateTime.parse(parts[5]), LocalDateTime.parse(parts[6])));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading metadata for ID: " + id);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(METADATA_FILE))) {
            for (Metadata md : metadataList) {
                bw.write(md.getId() + "," + md.getName() + "," + md.getLocation() + ","
                        + md.getWidth() + "," + md.getHeight() + ","
                        + md.getCreatedDateTime() + "," + md.getModifiedDateTime() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error loading metadata for ID: " + id);
        }
    }


}
