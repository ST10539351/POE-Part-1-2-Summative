package com.poe.part2;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/** The fourth main-menu option: "Stored Messages". Console only, no GUI. */
public class StoredMessagesMenu {

    public static void show(Scanner scanner, MessageManager manager, String jsonFile) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Stored Messages ---");
            System.out.println("1) Display sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search all messages for a recipient");
            System.out.println("5) Delete a message using its hash");
            System.out.println("6) Display report of all stored messages");
            System.out.println("7) Back");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1":
                    System.out.println(manager.displaySendersAndRecipients());
                    break;
                case "2":
                    System.out.println(manager.displayLongestStoredMessage());
                    break;
                case "3":
                    System.out.print("Enter message ID: ");
                    System.out.println(manager.searchMessageId(scanner.nextLine().trim()));
                    break;
                case "4":
                    System.out.print("Enter recipient number: ");
                    List<String> found = manager.searchByRecipient(scanner.nextLine().trim());
                    if (found.isEmpty()) {
                        System.out.println("No messages found for that recipient.");
                    } else {
                        for (String text : found) {
                            System.out.println("- " + text);
                        }
                    }
                    break;
                case "5":
                    System.out.print("Enter message hash: ");
                    String result = manager.deleteByHash(scanner.nextLine().trim());
                    System.out.println(result);
                    if (result.endsWith("successfully deleted.")) {
                        saveStored(manager, jsonFile); // keep the JSON file in step with the array
                    }
                    break;
                case "6":
                    System.out.println(manager.displayReport());
                    break;
                case "7":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    private static void saveStored(MessageManager manager, String jsonFile) {
        try {
            JsonMessageLoader.save(jsonFile, manager.getStoredMessages());
        } catch (IOException e) {
            System.out.println("Could not update " + jsonFile + ": " + e.getMessage());
        }
    }
}
