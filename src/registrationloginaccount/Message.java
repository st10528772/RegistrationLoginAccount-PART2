package registrationloginaccount;

import java.util.Random;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Message class for QuickChat (Part 2). Holds the details of one chat message.
 */
public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    // Shared by every message while the program is running.
    // These arrays are filled while the program runs (nothing is hard-coded).
    private static ArrayList<Message> sentMessages = new ArrayList<>();
    private static ArrayList<Message> disregardedMessages = new ArrayList<>();
    private static ArrayList<Message> storedMessages = new ArrayList<>();
    private static ArrayList<String> messageHashes = new ArrayList<>();
    private static ArrayList<String> messageIDs = new ArrayList<>();
    private static int totalMessages = 0;
    private static String storageFile = "storedMessages.json";

    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = "";
        this.messageHash = "";
    }

    public String getMessageID() {
        return messageID;
    }

    // Lets a unit test set a known message ID
    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }

    /**
     * Creates a random ten-digit message ID, one digit at a time, and returns
     * the confirmation text.
     */
    public String createMessageID() {
        Random random = new Random();
        String id = "";

        for (int i = 0; i < 10; i++) {
            id = id + random.nextInt(10);
        }

        this.messageID = id;
        return "Message ID generated: " + id;
    }

    /**
     * Checks that the message ID is not empty and is no more than ten
     * characters.
     */
    public boolean checkMessageID() {
        return messageID.length() > 0 && messageID.length() <= 10;
    }

    /**
     * Checks the recipient's cell number by reusing the Part 1 check
     * (international code +27 followed by nine digits).
     */
    public String checkRecipientCell() {
        if (Login.checkCellPhoneNumber(recipient)) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    /**
     * Builds the message hash: first two digits of the ID, the message number,
     * then the first and last words of the message, all in capital letters.
     */
    public String createMessageHash() {
        if (messageID.length() < 2 || messageText.trim().isEmpty()) {
            return "";
        }

        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^A-Za-z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");
        String idStart = messageID.substring(0, 2);

        messageHash = (idStart + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
        return messageHash;
    }

    /**
     * Checks that the message is no more than 250 characters. If it is too
     * long, says by how many characters.
     */
    public String checkMessageLength() {
        int length = messageText.length();

        if (length <= 250) {
            return "Message ready to send.";
        }
        return "Message exceeds 250 characters by " + (length - 250) + "; please reduce the size.";
    }

    /**
     * Returns the full details of this message in the required order: Message
     * ID, Message Hash, Recipient, Message.
     */
    public String getMessageDetails() {
        return "Message ID: " + messageID + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + messageText;
    }

    /**
     * Returns all the messages sent while the program is running.
     */
    public static String printMessages() {
        String allMessages = "";

        for (int i = 0; i < sentMessages.size(); i++) {
            allMessages = allMessages + sentMessages.get(i).getMessageDetails() + "\n\n";
        }
        return allMessages;
    }

    /**
     * Returns the total number of messages sent.
     */
    public static int returnTotalMessages() {
        return totalMessages;
    }

    /**
     * Saves this message to the JSON file. Messages already in the file are
     * kept. Gson usage based on the Gson User Guide [1].
     */
    // [1] Google, "Gson User Guide," GitHub. [Online]. Available:
    //     https://github.com/google/gson/blob/main/UserGuide.md [Accessed: Oct. 15, 2026].
    public String storeMessage() {
        loadStoredMessages();
        storedMessages.add(this);
        recordIdentifiers();
        return saveStoredMessages();
    }

    /**
     * Handles what the user chose to do with the message: 1 = send, 2 =
     * disregard, 3 = store to send later. The message is also added to the
     * matching array.
     */
    public String sentMessage(int choice) {
        if (choice == 1) {
            sentMessages.add(this);
            totalMessages++;
            recordIdentifiers();
            return "Message successfully sent.";
        } else if (choice == 2) {
            disregardedMessages.add(this);
            recordIdentifiers();
            return "Press 0 to delete the message.";
        } else if (choice == 3) {
            return storeMessage();
        }
        return "Invalid option. Please choose 1, 2 or 3.";
    }

    /**
     * Adds this message's ID and hash to the Message ID and Message Hash
     * arrays. Empty values and duplicates are skipped.
     */
    private void recordIdentifiers() {
        if (!messageID.isEmpty() && !messageIDs.contains(messageID)) {
            messageIDs.add(messageID);
        }
        if (!messageHash.isEmpty() && !messageHashes.contains(messageHash)) {
            messageHashes.add(messageHash);
        }
    }

    /**
     * Empties every array and resets the message counter. Used by the unit
     * tests so each test starts from a clean state.
     */
    public static void clearAll() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        totalMessages = 0;
    }

    /**
     * Lets a unit test point the JSON file somewhere else so the real file is
     * not touched.
     */
    public static void setStorageFile(String fileName) {
        storageFile = fileName;
    }

    /**
     * Reads the JSON file into the stored messages array and returns how many
     * messages were read. Gson usage based on the Gson User Guide [1].
     */
    public static int loadStoredMessages() {
        storedMessages.clear();
        File file = new File(storageFile);

        if (!file.exists()) {
            return 0;
        }

        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Message[] fromFile = gson.fromJson(reader, Message[].class);

            if (fromFile != null) {
                for (int i = 0; i < fromFile.length; i++) {
                    storedMessages.add(fromFile[i]);
                    fromFile[i].recordIdentifiers();
                }
            }
        } catch (IOException e) {
            System.out.println("Stored messages could not be read: " + e.getMessage());
        }
        return storedMessages.size();
    }

    /**
     * Writes the stored messages array to the JSON file.
     */
    private static String saveStoredMessages() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(storageFile)) {
            gson.toJson(storedMessages, writer);
            return "Message successfully stored.";
        } catch (IOException e) {
            return "Message could not be stored: " + e.getMessage();
        }
    }
    // ---------- Part 3: stored messages menu ----------

    /**
     * Option a: lists the sender and recipient of every stored message.
     */
    public static String displaySenderAndRecipient(String sender) {
        if (storedMessages.isEmpty()) {
            return "There are no stored messages.";
        }

        String result = "";
        for (int i = 0; i < storedMessages.size(); i++) {
            result = result + "Sender: " + sender
                    + " | Recipient: " + storedMessages.get(i).getRecipient() + "\n";
        }
        return result;
    }

    /**
     * Option b: returns the longest stored message.
     */
    public static String displayLongestMessage() {
        if (storedMessages.isEmpty()) {
            return "There are no stored messages.";
        }

        String longest = storedMessages.get(0).getMessageText();
        for (int i = 1; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).getMessageText().length() > longest.length()) {
                longest = storedMessages.get(i).getMessageText();
            }
        }
        return longest;
    }

    /**
     * Option c: searches for a message ID and returns the recipient and the
     * message.
     */
    public static String searchByMessageID(String idToFind) {
        ArrayList<Message> everything = new ArrayList<>();
        everything.addAll(sentMessages);
        everything.addAll(storedMessages);
        everything.addAll(disregardedMessages);

        if (messageIDs.contains(idToFind)) {
            for (int i = 0; i < everything.size(); i++) {
                if (everything.get(i).getMessageID().equals(idToFind)) {
                    return "Recipient: " + everything.get(i).getRecipient()
                            + "\nMessage: " + everything.get(i).getMessageText();
                }
            }
        }
        return "Message ID not found.";
    }

    /**
     * Option d: returns the text of every stored message for one recipient.
     */
    public static ArrayList<String> searchByRecipient(String recipientToFind) {
        ArrayList<String> found = new ArrayList<>();

        for (int i = 0; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).getRecipient().equals(recipientToFind)) {
                found.add(storedMessages.get(i).getMessageText());
            }
        }
        return found;
    }

    /**
     * Option e: deletes a stored message using its message hash, then updates
     * the JSON file.
     */
    public static String deleteByMessageHash(String hashToDelete) {
        for (int i = 0; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).getMessageHash().equals(hashToDelete)) {
                Message removed = storedMessages.remove(i);
                messageHashes.remove(removed.getMessageHash());
                messageIDs.remove(removed.getMessageID());
                saveStoredMessages();
                return "Message: \"" + removed.getMessageText() + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    /**
     * Option f: report with the full details of all stored messages.
     */
    public static String displayStoredReport() {
        String report = "";

        for (int i = 0; i < storedMessages.size(); i++) {
            report = report + storedMessages.get(i).getMessageDetails() + "\n\n";
        }
        return report;
    }

    // The next three methods give the unit tests read-only access to the arrays
    public static ArrayList<String> getSentMessageTexts() {
        ArrayList<String> texts = new ArrayList<>();

        for (int i = 0; i < sentMessages.size(); i++) {
            texts.add(sentMessages.get(i).getMessageText());
        }
        return texts;
    }

    public static ArrayList<String> getMessageHashes() {
        return new ArrayList<>(messageHashes);
    }

    public static ArrayList<String> getMessageIDs() {
        return new ArrayList<>(messageIDs);
    }
}
