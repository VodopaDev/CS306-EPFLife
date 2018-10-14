package ch.epfl.sweng.zuluzulu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ch.epfl.sweng.zuluzulu.Structure.ChatMessage;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class ChatMessageTest {

    private static final String senderName1 = "James";
    private static final String message1 = "James message";
    private static final String senderName2 = "Bond";
    private static final String message2 = "Bond's message";

    @Test
    public void testGuettersAndSetters() {
        ChatMessage chatMessage = new ChatMessage(senderName1, message1);

        assertEquals(senderName1, chatMessage.getSenderName());
        assertEquals(message1, chatMessage.getMessage());

        chatMessage.setSenderName(senderName2);
        chatMessage.setMessage(message2);

        assertEquals(senderName2, chatMessage.getSenderName());
        assertEquals(message2, chatMessage.getMessage());
    }

}
