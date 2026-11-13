package com.poe.part2;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static final String JSON_FILE = "messages.json"; // same file Message.storeMessage() writes

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();

        // ----- Part 1: register, then log in -----
        System.out.println("---Registration ---");
        register(scanner, login);

        System.out.println("\n--Login--");
        if (!logIn(scanner, login)) {
            System.out.println("Too many failed attempts. Goodbye.");
            return;
        }

        System.out.println("\nWelcome to QuickChat.");
        int limit = askPositiveNumber(scanner, "How many messages do you wish to enter? ");

        // ----- Part 3: arrays, loaded from the JSON file where messages were stored -----
        MessageManager manager = new MessageManager(login.getFirstName() + " " + login.getLastName());
        loadStoredMessages(manager);

        int entered = 0;
        boolean running = true;
        while (running) {
            System.out.println("\n--- Menu ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1":
                    if (entered >= limit) {
                        System.out.println("You have already entered all " + limit + " messages.");
                    } else {
                        entered = sendMessages(scanner, manager, entered, limit);
                        System.out.println("\nTotal number of messages sent: " + Message.returnTotalMessages());
                    }
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    running = false;
                    break;
                case "4":
                    StoredMessagesMenu.show(scanner, manager, JSON_FILE);
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
        System.out.println("Goodbye.");
        scanner.close();
    }

    // ---------- Part 1 helpers ----------

    private static void register(Scanner scanner, Login login) {
        String firstName = prompt(scanner, "Enter first name: ");
        String lastName = prompt(scanner, "Enter last name: ");

        String username;
        do {
            username = prompt(scanner, "Enter username (underscore, max 5 characters): ");
            System.out.println(login.usernameMessage(username));
        } while (!login.checkUserName(username));

        String password;
        do {
            password = prompt(scanner, "Enter password (8+ characters, capital, number, special character): ");
            System.out.println(login.passwordMessage(password));
        } while (!login.checkPasswordComplexity(password));

        String cell;
        do {
            cell = prompt(scanner, "Enter cell phone number (e.g. +27838968976): ");
            System.out.println(login.cellMessage(cell));
        } while (!login.checkCellPhoneNumber(cell));

        System.out.println(login.registerUser(firstName, lastName, username, password, cell));
    }

    private static boolean logIn(Scanner scanner, Login login) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            String username = prompt(scanner, "Username: ");
            String password = prompt(scanner, "Password: ");
            System.out.println(login.returnLoginStatus(username, password));
            if (login.loginUser(username, password)) {
                return true;
            }
        }
        return false;
    }

    // ---------- Part 2 helpers ----------

    /** Enters the remaining messages, up to the limit the user set. Returns how many have been entered. */
    private static int sendMessages(Scanner scanner, MessageManager manager, int entered, int limit) {
        while (entered < limit) {
            System.out.println("\n--- Message " + (entered + 1) + " of " + limit + " ---");

            // Recipient (international code, no more than ten characters)
            String recipient;
            while (true) {
                recipient = prompt(scanner, "Recipient cell number: ");
                String status = new Message(entered, recipient, "").checkRecipientCell();
                System.out.println(status);
                if (status.equals(Message.CELL_OK)) {
                    break;
                }
            }

            // Message text (250 characters at most)
            Message m;
            while (true) {
                String text = prompt(scanner, "Message: ");
                m = new Message(entered, recipient, text);
                if (m.checkMessageLength().equals(Message.MESSAGE_READY)) {
                    System.out.println(Message.MESSAGE_READY);
                    break;
                }
                System.out.println("Please enter a message of less than 250 characters.");
            }
            System.out.println(m.messageIDStatus());

            int choice = askChoice(scanner);
            String result = m.sentMessage(choice);
            System.out.println(result);

            if (choice == 1) {
                manager.addSent(m);
                System.out.println("\n" + m.details());
            } else if (choice == 2) {
                manager.addDisregarded(m);
                while (!prompt(scanner, "").equals("0")) {
                    System.out.println("Press 0 to delete the message.");
                }
                System.out.println("Message deleted.");
            } else if (result.equals("Message successfully stored.")) {
                manager.addStored(m);
            }
            entered++;
        }
        return entered;
    }

    private static int askChoice(Scanner scanner) {
        while (true) {
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            String input = prompt(scanner, "Choose an option: ");
            if (input.equals("1") || input.equals("2") || input.equals("3")) {
                return Integer.parseInt(input);
            }
            System.out.println("Invalid option.");
        }
    }

    private static int askPositiveNumber(Scanner scanner, String question) {
        while (true) {
            String input = prompt(scanner, question);
            try {
                int number = Integer.parseInt(input);
                if (number > 0) {
                    return number;
                }
            } catch (NumberFormatException e) {
                // fall through to the error message
            }
            System.out.println("Please enter a whole number greater than 0.");
        }
    }

    // ---------- Part 3 helper ----------

    private static void loadStoredMessages(MessageManager manager) {
        try {
            for (Message m : JsonMessageLoader.load(JSON_FILE)) {
                manager.addStored(m);
            }
        } catch (IOException e) {
            // no messages.json yet: nothing has been stored
        } catch (RuntimeException e) {
            System.out.println("Could not read " + JSON_FILE + ": " + e.getMessage());
        }
    }

    private static String prompt(Scanner scanner, String text) {
        System.out.print(text);
        return scanner.nextLine().trim();
    }
}
