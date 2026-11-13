package com.poe.part2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/** Part 2 unit tests, using the test data from the brief. */
class MessageTest {

    @TempDir
    Path tempDir;

    private Message task1;   // Test Data for Task 1 (sent)
    private Message task2;   // Test Data for Message 2 (discarded)

    @BeforeEach
    void setUp() {
        Message.resetSession();
        Message.setStorageFile(tempDir.resolve("messages.json")); // never touch the real messages.json
        task1 = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        task2 = new Message("0187654321", 1, "08575975889", "Hi Keegan, did you receive the payment?");
    }

    // ---------- message length ----------

    @Test
    void messageUnder250CharactersSucceeds() {
        assertEquals("Message ready to send.", task1.checkMessageLength());
    }

    @Test
    void messageOf250CharactersStillSucceeds() {
        Message m = new Message("0012345678", 0, "+27718693002", "a".repeat(250));
        assertEquals("Message ready to send.", m.checkMessageLength());
    }

    @Test
    void messageOver250CharactersFails() {
        Message m = new Message("0012345678", 0, "+27718693002", "a".repeat(260));
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", m.checkMessageLength());
    }

    // ---------- recipient number ----------

    @Test
    void recipientNumberCorrectlyFormatted() {
        assertEquals("Cell phone number successfully captured.", task1.checkRecipientCell());
    }

    @Test
    void recipientNumberIncorrectlyFormatted() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. "
                + "Please correct the number and try again.", task2.checkRecipientCell());
    }

    // ---------- message hash ----------

    @Test
    void messageHashForTestCase1() {
        assertEquals("00:0:HITONIGHT", task1.createMessageHash());
    }

    @Test
    void remainingMessageHashesInALoop() {
        String[] ids = {"0012345678", "0187654321", "2299999999"};
        int[] numbers = {0, 1, 2};
        String[] texts = {"Hi Mike, can you join us for dinner tonight?",
                "Hi Keegan, did you receive the payment?",
                "Where are you? You are late! I have asked you to be on time."};
        String[] expected = {"00:0:HITONIGHT", "01:1:HIPAYMENT", "22:2:WHERETIME"};

        for (int i = 0; i < ids.length; i++) {
            Message m = new Message(ids[i], numbers[i], "+27718693002", texts[i]);
            assertEquals(expected[i], m.createMessageHash());
        }
    }

    // ---------- message ID ----------

    @Test
    void messageIdIsCreated() {
        Message m = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ID generated: " + m.getMessageID(), m.messageIDStatus());
        assertTrue(m.getMessageID().matches("\\d{10}"));
        assertTrue(m.checkMessageID());
    }

    @Test
    void messageIdLongerThanTenCharactersFails() {
        Message m = new Message("12345678901", 0, "+27718693002", "Hello there");
        assertFalse(m.checkMessageID());
    }

    // ---------- message sent ----------

    @Test
    void userSelectsSendMessage() {
        assertEquals("Message successfully sent.", task1.sentMessage(1));
    }

    @Test
    void userSelectsDisregardMessage() {
        assertEquals("Press 0 to delete the message.", task2.sentMessage(2));
    }

    @Test
    void userSelectsStoreMessage() throws IOException {
        assertEquals("Message successfully stored.", task1.sentMessage(3));
        String json = new String(Files.readAllBytes(tempDir.resolve("messages.json")), StandardCharsets.UTF_8);
        assertTrue(json.contains("Hi Mike, can you join us for dinner tonight?"));
    }

    // ---------- totals and printing ----------

    @Test
    void totalMessagesSentOnlyCountsSentMessages() {
        task1.sentMessage(1);   // sent
        task2.sentMessage(2);   // disregarded
        assertEquals(1, Message.returnTotalMessages());
    }

    @Test
    void printMessagesListsEverythingSent() {
        assertEquals("No messages have been sent.", Message.printMessages());
        task1.sentMessage(1);
        String printed = Message.printMessages();
        assertTrue(printed.contains("00:0:HITONIGHT"));
        assertTrue(printed.contains("+27718693002"));
    }
}
