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

    // Shared by every message while the program is running
    private static ArrayList<Message> sentMessages = new ArrayList<>();
    private static int totalMessages = 0;
    private static final String STORAGE_FILE = "storedMessages.json";

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
     * Saves this message to a JSON file. Messages already in the file are kept.
     * Gson usage based on the Gson User Guide [1].
     */
    // [1] Google, "Gson User Guide," GitHub. [Online]. Available:
    //     https://github.com/google/gson/blob/main/UserGuide.md [Accessed: Oct. 15, 2026].
    public String storeMessage() {
        ArrayList<Message> storedMessages = new ArrayList<>();
        File file = new File(STORAGE_FILE);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            if (file.exists()) {
                try (FileReader reader = new FileReader(file)) {
                    Message[] existing = gson.fromJson(reader, Message[].class);
                    if (existing != null) {
                        for (int i = 0; i < existing.length; i++) {
                            storedMessages.add(existing[i]);
                        }
                    }
                }
            }

            storedMessages.add(this);

            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(storedMessages, writer);
            }
            return "Message successfully stored.";
        } catch (IOException e) {
            return "Message could not be stored: " + e.getMessage();
        }
    }

    /**
     * Handles what the user chose to do with the message: 1 = send, 2 =
     * disregard, 3 = store to send later.
     */
    public String sentMessage(int choice) {
        if (choice == 1) {
            sentMessages.add(this);
            totalMessages++;
            return "Message successfully sent.";
        } else if (choice == 2) {
            return "Press 0 to delete the message.";
        } else if (choice == 3) {
            return storeMessage();
        }
        return "Invalid option. Please choose 1, 2 or 3.";
    }

}
