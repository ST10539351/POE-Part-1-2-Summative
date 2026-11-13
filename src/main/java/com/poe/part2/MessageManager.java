package com.poe.part2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Part 3: holds the five arrays (sent, disregarded, stored, hashes, IDs) and the
 * "Stored Messages" features. Nothing is hard-coded: every array is filled through
 * addSent / addDisregarded / addStored.
 */
public class MessageManager {

    private final List<Message> sentMessages = new ArrayList<>();
    private final List<Message> disregardedMessages = new ArrayList<>();
    private final List<Message> storedMessages = new ArrayList<>();
    private final List<String> messageHashes = new ArrayList<>();
    private final List<String> messageIds = new ArrayList<>();

    // The logged-in user is the sender of every message.
    private final String sender;

    public MessageManager(String sender) {
        this.sender = sender;
    }

    public void addSent(Message m) {
        sentMessages.add(m);
        track(m);
    }

    public void addDisregarded(Message m) {
        disregardedMessages.add(m);
        track(m);
    }

    public void addStored(Message m) {
        storedMessages.add(m);
        track(m);
    }

    private void track(Message m) {
        messageHashes.add(m.getMessageHash());
        messageIds.add(m.getMessageID());
    }

    public int getMessageCount() {
        return messageIds.size();
    }

    public List<Message> getStoredMessages() {
        return new ArrayList<>(storedMessages);
    }

    public List<String> getSentMessageTexts() {
        return textsOf(sentMessages);
    }

    public List<String> getDisregardedMessageTexts() {
        return textsOf(disregardedMessages);
    }

    public List<String> getStoredMessageTexts() {
        return textsOf(storedMessages);
    }

    public List<String> getMessageHashes() {
        return new ArrayList<>(messageHashes);
    }

    public List<String> getMessageIds() {
        return new ArrayList<>(messageIds);
    }

    /** 2a. Sender and recipient of every stored message. */
    public String displaySendersAndRecipients() {
        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : storedMessages) {
            sb.append("Sender: ").append(sender)
              .append(" | Recipient: ").append(m.getRecipient()).append("\n");
        }
        return sb.toString().trim();
    }

    /** 2b. The longest stored message (by number of characters). */
    public String displayLongestStoredMessage() {
        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }
        Message longest = storedMessages.get(0);
        for (Message m : storedMessages) {
            if (m.getMessage().length() > longest.getMessage().length()) {
                longest = m;
            }
        }
        return longest.getMessage();
    }

    /** 2c. Finds a message by its ID (searches sent, disregarded and stored). */
    public Message findByMessageId(String id) {
        for (Message m : allMessages()) {
            if (m.getMessageID().equals(id)) {
                return m;
            }
        }
        return null;
    }

    public String searchMessageId(String id) {
        Message m = findByMessageId(id);
        if (m == null) {
            return "No message found with ID " + id + ".";
        }
        return "Recipient: " + m.getRecipient() + "\nMessage: " + m.getMessage();
    }

    /** 2d. Every sent or stored message for one recipient. */
    public List<String> searchByRecipient(String recipient) {
        List<String> results = new ArrayList<>();
        for (Message m : sentMessages) {
            if (m.getRecipient().equals(recipient)) {
                results.add(m.getMessage());
            }
        }
        for (Message m : storedMessages) {
            if (m.getRecipient().equals(recipient)) {
                results.add(m.getMessage());
            }
        }
        return results;
    }

    /** 2e. Deletes a message using its hash and removes it from every array. */
    public String deleteByHash(String hash) {
        List<List<Message>> lists = new ArrayList<>();
        lists.add(sentMessages);
        lists.add(disregardedMessages);
        lists.add(storedMessages);

        for (List<Message> list : lists) {
            Iterator<Message> it = list.iterator();
            while (it.hasNext()) {
                Message m = it.next();
                if (m.getMessageHash().equals(hash)) {
                    it.remove();
                    messageHashes.remove(m.getMessageHash());
                    messageIds.remove(m.getMessageID());
                    return "Message: \"" + stripTrailingFullStop(m.getMessage())
                            + "\" successfully deleted.";
                }
            }
        }
        return "No message found with hash " + hash + ".";
    }

    /** 2f. Full details of every stored message. */
    public String displayReport() {
        StringBuilder sb = new StringBuilder("===== Stored Messages Report =====\n");
        if (storedMessages.isEmpty()) {
            sb.append("No stored messages.\n");
        }
        for (Message m : storedMessages) {
            sb.append("Message Hash: ").append(m.getMessageHash()).append("\n")
              .append("Message ID: ").append(m.getMessageID()).append("\n")
              .append("Sender: ").append(sender).append("\n")
              .append("Recipient: ").append(m.getRecipient()).append("\n")
              .append("Message: ").append(m.getMessage()).append("\n")
              .append("----------------------------------\n");
        }
        return sb.toString();
    }

    private List<Message> allMessages() {
        List<Message> all = new ArrayList<>(sentMessages);
        all.addAll(disregardedMessages);
        all.addAll(storedMessages);
        return all;
    }

    private static List<String> textsOf(List<Message> list) {
        List<String> texts = new ArrayList<>();
        for (Message m : list) {
            texts.add(m.getMessage());
        }
        return texts;
    }

    // The brief's delete message shows the text without its final full stop.
    private static String stripTrailingFullStop(String text) {
        return text.endsWith(".") ? text.substring(0, text.length() - 1) : text;
    }
}
