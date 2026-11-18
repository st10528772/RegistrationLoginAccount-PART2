package registrationloginaccount;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Part 3 unit tests, using the five test messages from the brief. Flags: 1 =
 * Sent, 2 = Disregard, 3 = Stored (same codes as sentMessage()).
 */
public class MessageArrayTest {

    // A fresh temporary folder for each test, so the real JSON file is not touched
    @TempDir
    Path tempFolder;

    @BeforeEach
    public void setUp() {
        Message.clearAll();
        Message.setStorageFile(tempFolder.resolve("storedMessages.json").toString());

        addTestMessage(1, "+27834557896", "Did you get the cake?", "0000000001", 1);
        addTestMessage(2, "+27838884567", "Where are you? You are late! I have asked you to be on time.", "0000000002", 3);
        addTestMessage(3, "+27834484567", "Yohoooo, I am at your gate.", "0000000003", 2);
        addTestMessage(4, "0838884567", "It is dinner time!", "0838884567", 1);
        addTestMessage(5, "+27838884567", "Ok, I am leaving without you.", "0000000005", 3);
    }

    // Builds a message, gives it an ID and hash, then sends/disregards/stores it
    private void addTestMessage(int number, String recipient, String text, String id, int flag) {
        Message message = new Message(number, recipient, text);
        message.setMessageID(id);
        message.createMessageHash();
        message.sentMessage(flag);
    }

    @Test
    public void testSentMessagesArrayPopulated() {
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
                "Did you get the cake?", "It is dinner time!"));
        assertEquals(expected, Message.getSentMessageTexts());
    }

    @Test
    public void testStoredMessagesReadFromJsonFile() {
        // Messages 2 and 5 were saved to the JSON file; read them back
        assertEquals(2, Message.loadStoredMessages());
    }

    @Test
    public void testMessageHashAndIDArraysPopulated() {
        assertEquals(5, Message.getMessageHashes().size());
        assertEquals(5, Message.getMessageIDs().size());
        assertTrue(Message.getMessageHashes().contains("00:2:WHERETIME"));
    }

    @Test
    public void testDisplayLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                Message.displayLongestMessage());
    }

    @Test
    public void testSearchByMessageID() {
        assertEquals("Recipient: 0838884567\nMessage: It is dinner time!",
                Message.searchByMessageID("0838884567"));
    }

    @Test
    public void testSearchByMessageIDNotFound() {
        assertEquals("Message ID not found.", Message.searchByMessageID("9999999999"));
    }

    @Test
    public void testSearchByRecipient() {
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
                "Where are you? You are late! I have asked you to be on time.",
                "Ok, I am leaving without you."));
        assertEquals(expected, Message.searchByRecipient("+27838884567"));
    }

    @Test
    public void testDeleteByMessageHash() {
        String hash = "00:2:WHERETIME";

        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
                Message.deleteByMessageHash(hash));

        // It is really gone, from the array and from the JSON file
        assertEquals("Message hash not found.", Message.deleteByMessageHash(hash));
        assertEquals(1, Message.loadStoredMessages());
    }

    @Test
    public void testDisplayReportSentMessages() {
        String report = Message.printMessages();

        assertTrue(report.contains("Message Hash: 00:1:DIDCAKE"));
        assertTrue(report.contains("Recipient: +27834557896"));
        assertTrue(report.contains("Message: Did you get the cake?"));
        assertTrue(report.contains("Message: It is dinner time!"));
    }

    @Test
    public void testDisplayReportStoredMessages() {
        String report = Message.displayStoredReport();

        assertTrue(report.contains("Message Hash: 00:2:WHERETIME"));
        assertTrue(report.contains("Recipient: +27838884567"));
        assertTrue(report.contains("Message: Ok, I am leaving without you."));
    }

    @Test
    public void testDisplaySenderAndRecipient() {
        String result = Message.displaySenderAndRecipient("+27718693002");
        assertTrue(result.contains("Sender: +27718693002 | Recipient: +27838884567"));
    }
}
