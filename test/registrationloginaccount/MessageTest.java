package registrationloginaccount;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    public void testRecipientCellFailure() {
        Message m = new Message(1, "08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", m.checkRecipientCell());
    }
}
