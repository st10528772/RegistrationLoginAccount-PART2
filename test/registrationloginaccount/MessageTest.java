package registrationloginaccount;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class MessageTest {

    // ---------- Message length ----------
    @Test
    public void testMessageLengthSuccess() {
        Message m = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", m.checkMessageLength());
    }

    @Test
    public void testMessageLengthExactly250IsAccepted() {
        Message m = new Message(0, "+27718693002", "a".repeat(250));
        assertEquals("Message ready to send.", m.checkMessageLength());
    }

    @Test
    public void testMessageLength251IsTooLong() {
        Message m = new Message(0, "+27718693002", "a".repeat(251));
        assertEquals("Message exceeds 250 characters by 1; please reduce the size.", m.checkMessageLength());
    }

    @Test
    public void testMessageLengthFailure() {
        Message m = new Message(0, "+27718693002", "a".repeat(260));
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", m.checkMessageLength());
    }

    // ---------- Recipient cell number ----------
    @Test
    public void testRecipientCellSuccess() {
        Message m = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Cell phone number successfully captured.", m.checkRecipientCell());
    }

    // ---------- Message hash ----------
    @Test
    public void testMessageHashTestCase1() {
        Message m = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        m.setMessageID("0012345678");
        assertEquals("00:0:HITONIGHT", m.createMessageHash());
    }

    @Test
    public void testMessageHashLoop() {
        int[] numbers = {0, 1, 2};
        String[] ids = {"0012345678", "0012345678", "7512345678"};
        String[] texts = {
            "Hi Mike, can you join us for dinner tonight?",
            "Hi Keegan, did you receive the payment?",
            "Hello"
        };
        String[] expected = {"00:0:HITONIGHT", "00:1:HIPAYMENT", "75:2:HELLOHELLO"};

        for (int i = 0; i < numbers.length; i++) {
            Message m = new Message(numbers[i], "+27718693002", texts[i]);
            m.setMessageID(ids[i]);
            assertEquals(expected[i], m.createMessageHash());
        }
    }

    // ---------- Message ID ----------
    @Test
    public void testMessageIDCreated() {
        Message m = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        String result = m.createMessageID();

        assertEquals("Message ID generated: " + m.getMessageID(), result);
        assertEquals(10, m.getMessageID().length());
        assertTrue(m.checkMessageID());
    }

    // ---------- Send, disregard, store ----------
    @Test
    public void testSendMessage() {
        Message m = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully sent.", m.sentMessage(1));
    }

    @Test
    public void testDisregardMessage() {
        Message m = new Message(1, "08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Press 0 to delete the message.", m.sentMessage(2));
    }

    @Test
    public void testStoreMessage() {
        Message m = new Message(2, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully stored.", m.sentMessage(3));
    }

    @Test
    public void testInvalidOption() {
        Message m = new Message(0, "+27718693002", "Hello");
        assertEquals("Invalid option. Please choose 1, 2 or 3.", m.sentMessage(9));
    }

    // ---------- Total messages ----------
    @Test
    public void testTotalMessagesIncreasesOnlyWhenSent() {
        int before = Message.returnTotalMessages();

        new Message(0, "+27718693002", "Hello").sentMessage(1);
        assertEquals(before + 1, Message.returnTotalMessages());

        new Message(1, "+27718693002", "Hello").sentMessage(2);
        assertEquals(before + 1, Message.returnTotalMessages());
    }

    @Test
    public void testRecipientCellFailure() {
        Message m = new Message(1, "08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", m.checkRecipientCell());
    }
}
