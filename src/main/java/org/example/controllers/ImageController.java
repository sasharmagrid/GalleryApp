package org.example.controllers;

import org.example.models.Image;
import org.example.models.Metadata;
import org.example.services.MetadataService;
import org.example.services.StorageService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ImageController {
    public static void listImages() {
        List<Image> images = StorageService.listImages();
        for (Image image : images) {
            // Fetch metadata for each image by its ID
            Metadata metadata = MetadataService.getMetadata(image.getId());
            if (metadata != null) {
//                System.out.printf("| %-3s | %-12s | %-20s | %-10s |%n",
//                        image.getId(), image.getName(), image.getFilePath(),
//                        "Found");

                System.out.println("ID: " + image.getId() + ", Name: " + image.getName() + ", File Path: " + image.getFilePath() +
                        ", Metadata Name: " + metadata.getName() + ", Metadata Location: " + metadata.getLocation());
            } else {
//                System.out.printf("| %-3s | %-12s | %-20s | %-10s |%n",
//                        image.getId(), image.getName(), image.getFilePath(),
//                        "Not Found");

                System.out.println("ID: " + image.getId() + ", Name: " + image.getName() + ", File Path: " + image.getFilePath() +
                        ", Metadata not found.");
            }
        }
    }

    public static void renameImage() {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        System.out.print("Enter Image ID to rename: ");
        String id = scanner.nextLine();

        System.out.print("Enter new image name: ");
        String newName = scanner.nextLine();

        StorageService.renameImage(id, newName);
    }

    public static void getMetadata(String id) {
        Metadata metadata = MetadataService.getMetadata(id);
        if (metadata != null) {
            System.out.println("ID: " + metadata.getId()
                    + "\n Name: " + metadata.getName()
                    + "\n Location: " + metadata.getLocation()
                    + "\n Width: " + metadata.getWidth()
                    + "\n Height: " + metadata.getHeight()
                    + "\n Created Time: " + metadata.getCreatedDateTime()
                    + "\n Modified Time: " + metadata.getModifiedDateTime());
        } else {
            System.out.println("Metadata not found.");
        }
    }


    public static void updateMetadata(String id,String choose) {
        Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);
        Metadata metadata = MetadataService.getMetadata(id);
        if (metadata != null) {
            String newInfo;
            if(choose.equals("name")){
                newInfo = sc.nextLine();
                MetadataService.updateMetadata(id,newInfo, metadata.getLocation());
            }else if(choose.equals("location")){
                newInfo = sc.nextLine();
                MetadataService.updateMetadata(id, metadata.getName(), newInfo);
            }else {
                System.out.println("Invalid choice.");
            }

        } else {
            System.out.println("Metadata not found.");
        }
    }

    public static void deleteImage(String id) {
        if (StorageService.deleteImage(id)) {
            System.out.println("Image deleted successfully.");
        } else {
            System.out.println("Failed to delete image.");
        }
    }

    public static void groupImages() {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

        System.out.print("Enter group name: ");
        String groupName = scanner.nextLine();

        System.out.print("Enter image IDs to group (comma separated): ");
        String[] ids = scanner.nextLine().split(",");

        List<String> imageIds = Arrays.asList(ids);

        if (StorageService.groupImages(groupName, imageIds)) {
            System.out.println("Images grouped successfully into folder: " + groupName);
        } else {
            System.out.println("Failed to group images.");
        }
    }

    public static void viewImage() {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        System.out.print("Enter Image ID to view: ");
        String id = scanner.nextLine();

        StorageService.viewImage(id);
    }

}
