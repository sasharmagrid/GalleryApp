package org.example.services;

import org.example.models.Image;
import org.example.utils.FileUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class StorageService {
    private static final String IMAGE_FOLDER = "/Users/sasharma/Documents/GalleryApp/src/main/java/images";

    public static void viewImage(String id) {
        List<Image> images = FileUtil.loadImages();
        Image imageToView = images.stream()
                .filter(img -> img.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (imageToView != null) {
            File imageFile = new File(imageToView.getFilePath());

            if (imageFile.exists()) {
                try {
                    Desktop.getDesktop().open(imageFile);
                    System.out.println("Opening image: " + imageToView.getName());
                } catch (IOException e) {
                    System.out.println("Error opening image: " + e.getMessage());
                }
            } else {
                System.out.println("Image file not found.");
            }
        } else {
            System.out.println("Image ID not found.");
        }
    }

    public static List<Image> listImages() {
        return FileUtil.loadImages();
    }

    public static boolean deleteImage(String id) {
        List<Image> images = FileUtil.loadImages();
        Image toDelete = images.stream().filter(img -> img.getId().equals(id)).findFirst().orElse(null);

        if (toDelete != null) {
            File file = new File(toDelete.getFilePath());

            if (file.delete()) { // Only proceed if the image file is successfully deleted
                FileUtil.removeImage(id); // Remove from images.txt
                FileUtil.removeMetadata(id); // Remove from metadata.txt
                return true;
            }
        }
        return false;
    }

    public static boolean groupImages(String groupName, List<String> imageIds) {
        File groupFolder = new File(IMAGE_FOLDER + "/" + groupName);

        // Create the group folder if it doesn't exist
        if (!groupFolder.exists()) {
            if (!groupFolder.mkdirs()) {
                System.out.println("Failed to create group folder.");
                return false;
            }
        }

        List<Image> images = FileUtil.loadImages();
        boolean allMoved = true;

        for (String id : imageIds) {
            Image image = images.stream().filter(img -> img.getId().equals(id)).findFirst().orElse(null);

            if (image != null) {
                File oldFile = new File(image.getFilePath());
                File newFile = new File(groupFolder, oldFile.getName());

                // Move the image file to the new folder
                if (oldFile.renameTo(newFile)) {
                    // Update the image record
                    FileUtil.updateImageLocation(id, newFile.getAbsolutePath());

                    // Update the metadata file with the new location
                    FileUtil.updateMetadataLocation(id, newFile.getAbsolutePath());
                } else {
                    System.out.println("Failed to move image: " + image.getName());
                    allMoved = false;
                }
            }
        }
        return allMoved;
    }

    public static void renameImage(String id, String newName) {
        List<Image> images = FileUtil.loadImages();
        Image imageToRename = images.stream()
                .filter(img -> img.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (imageToRename != null) {
            File oldFile = new File(imageToRename.getFilePath());
            String fileExtension = oldFile.getName().substring(oldFile.getName().lastIndexOf('.')); // Preserve file extension
            File newFile = new File(oldFile.getParent(), newName + fileExtension);

            if (oldFile.renameTo(newFile)) {
                // Update the file path in records
                FileUtil.updateImageName(id, newName, newFile.getAbsolutePath());
                FileUtil.updateMetadataName(id, newName);
                System.out.println("Image renamed successfully to: " + newName);
            } else {
                System.out.println("Failed to rename image.");
            }
        } else {
            System.out.println("Image ID not found.");
        }
    }

}
