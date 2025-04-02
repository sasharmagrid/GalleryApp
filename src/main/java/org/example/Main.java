package org.example;

import org.example.controllers.ImageController;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.example.utils.LoadImagesUtil.initializeDataFiles;

public class Main {

    public static void main(String[] args) {

        initializeDataFiles();

        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        while (true) {
            System.out.println("1. List Images\n2. View Image\n3. Rename Image\n4. Delete Image\n5. Create Album\n6. Get Metadata\n7. Update Metadata\n8. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    ImageController.listImages();
                    break;
                case 2:
                    ImageController.viewImage();
                    break;
                case 3:
                    ImageController.renameImage();
                    break;
                case 4:
                    System.out.print("Enter Image ID: ");
                    String deleteId = scanner.nextLine();
                    ImageController.deleteImage(deleteId);
                    break;
                case 5:
                    ImageController.groupImages();
                    break;
                case 6:
                    System.out.print("Enter Image ID: ");
                    String metadataId = scanner.nextLine();
                    ImageController.getMetadata(metadataId);
                    break;
                case 7:
                    System.out.print("Enter Image ID: ");
                    String updateId = scanner.nextLine();
                    ImageController.getMetadata(updateId);
                    System.out.print("Update name or location: ");
                    String choose = scanner.nextLine();
                    ImageController.updateMetadata(updateId, choose);
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
