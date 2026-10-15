package registrationloginaccount;

import java.util.Random;

/**
 * Message class for QuickChat (Part 2). Holds the details of one chat message.
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

}
