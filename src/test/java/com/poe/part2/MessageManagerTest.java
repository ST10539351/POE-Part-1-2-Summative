package com.poe.part2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Part 3 unit tests, using the test data from the brief. */
class MessageManagerTest {

    private MessageManager manager;

    // Hash = first 2 digits of ID : message number : FIRSTWORD + LASTWORD
    private static final String HASH_MSG2 = "10:1:WHERETIME";

    @BeforeEach
    void setUp() {
        manager = new MessageManager("Developer");
        // Test data from the brief (fixed IDs so the hashes are predictable).
        manager.addSent(new Message("1000000001", 0, "+27834557896", "Did you get the cake?"));
        manager.addStored(new Message("1000000002", 1, "+27838884567",
                "Where are you? You are late! I have asked you to be on time."));
        manager.addDisregarded(new Message("1000000003", 2, "+27834484567", "Yohoooo, I am at your gate."));
        manager.addSent(new Message("0838884567", 3, "Developer", "It is dinner time!"));
        manager.addStored(new Message("1000000005", 4, "+27838884567", "Ok, I am leaving without you."));
    }

    @Test
    void sentMessagesArrayCorrectlyPopulated() {
        assertEquals(Arrays.asList("Did you get the cake?", "It is dinner time!"),
                manager.getSentMessageTexts());
    }

    @Test
    void disregardedAndHashAndIdArraysPopulated() {
        assertEquals(List.of("Yohoooo, I am at your gate."), manager.getDisregardedMessageTexts());
        assertEquals(5, manager.getMessageHashes().size());
        assertEquals(5, manager.getMessageIds().size());
    }

    @Test
    void displayLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                manager.displayLongestStoredMessage());
    }

    @Test
    void searchForMessageId() {
        assertEquals("It is dinner time!", manager.findByMessageId("0838884567").getMessage());
    }

    @Test
    void searchAllMessagesForRecipient() {
        assertEquals(Arrays.asList(
                "Where are you? You are late! I have asked you to be on time.",
                "Ok, I am leaving without you."),
                manager.searchByRecipient("+27838884567"));
    }

    @Test
    void deleteMessageUsingHash() {
        assertEquals(HASH_MSG2, manager.getMessageHashes().get(1));
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time\""
                + " successfully deleted.", manager.deleteByHash(HASH_MSG2));
        assertEquals(List.of("Ok, I am leaving without you."), manager.getStoredMessageTexts());
        assertEquals(4, manager.getMessageHashes().size());
        assertEquals(4, manager.getMessageIds().size());
    }

    @Test
    void deleteUnknownHash() {
        assertEquals("No message found with hash 99:9:NOPE.", manager.deleteByHash("99:9:NOPE"));
    }

    @Test
    void displayReport() {
        String expected = "===== Stored Messages Report =====\n"
                + "Message Hash: 10:1:WHERETIME\n"
                + "Message ID: 1000000002\n"
                + "Sender: Developer\n"
                + "Recipient: +27838884567\n"
                + "Message: Where are you? You are late! I have asked you to be on time.\n"
                + "----------------------------------\n"
                + "Message Hash: 10:4:OKYOU\n"
                + "Message ID: 1000000005\n"
                + "Sender: Developer\n"
                + "Recipient: +27838884567\n"
                + "Message: Ok, I am leaving without you.\n"
                + "----------------------------------\n";
        assertEquals(expected, manager.displayReport());
    }
}
