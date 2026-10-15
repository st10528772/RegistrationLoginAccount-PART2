package registrationloginaccount;

import java.util.Random;

/**
 * Message class for QuickChat (Part 2).
 * Holds the details of one chat message.
 */
public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

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
     * Creates a random ten-digit message ID, one digit at a time,
     * and returns the confirmation text.
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
     * Checks that the message ID is not empty and is no more than ten characters.
     */
    public boolean checkMessageID() {
        return messageID.length() > 0 && messageID.length() <= 10;
    }
}