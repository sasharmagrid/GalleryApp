package org.example.utils;

import org.example.models.Image;
import org.example.models.Metadata;

import java.io.File;

public class LoadImagesUtil {
    private static final String IMAGE_FOLDER = "/Users/sasharma/Documents/GalleryApp/src/main/java/images";
    private static final String IMAGE_DATA_FILE = "/Users/sasharma/Documents/GalleryApp/src/main/java/data/images.txt";
    private static final String METADATA_FILE = "/Users/sasharma/Documents/GalleryApp/src/main/java/data/metadata.txt";

    public static void initializeDataFiles() {
        File imageFolder = new File(IMAGE_FOLDER);
        File imageDataFile = new File(IMAGE_DATA_FILE);
        File metadataFile = new File(METADATA_FILE);

        if (imageDataFile.length() == 0 && metadataFile.length() == 0) {
            System.out.println("Populating image and metadata files...");

            File[] files = imageFolder.listFiles();
            if (files != null) {
                int idCounter = 1;
                for (File file : files) {
                    if (file.isFile()) {

                        String imageId = String.valueOf(idCounter);
                        String imageName = file.getName();
                        String filePath = file.getAbsolutePath();

                        FileUtil.addImage(new Image(imageId, imageName, filePath));

                        Metadata metadata = FileUtil.extractMetadata(file.getAbsolutePath());


                        if (metadata != null) {

                            metadata = new Metadata(
                                    imageId,
                                    imageName,
                                    filePath,
                                    metadata.getWidth(),
                                    metadata.getHeight(),
                                    metadata.getCreatedDateTime(),
                                    metadata.getModifiedDateTime()
                            );
                            FileUtil.addMetadata(metadata);
                        }

                        idCounter++;
                    }
                }
            }
        }
    }
}
