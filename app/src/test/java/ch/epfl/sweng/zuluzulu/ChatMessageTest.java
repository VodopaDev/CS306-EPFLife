package ch.epfl.sweng.zuluzulu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ch.epfl.sweng.zuluzulu.Structure.ChatMessage;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class ChatMessageTest {

    private static final String senderName1 = "James";
    private static final String sciper1 = "111111";
    private static final String message1 = "James message";
    private static final boolean ownMessage1 = false;
    private static final String senderName2 = "Bond";
    private static final String sciper2 = "222222";
    private static final boolean ownMessage2 = true;
    private static final String message2 = "Bond's message";

    @Test
    public void testGuettersAndSetters() {
        ChatMessage chatMessage = new ChatMessage(senderName1, sciper1, message1, ownMessage1);

        assertEquals(senderName1, chatMessage.getSenderName());
        assertEquals(sciper1, chatMessage.getSciper());
        assertEquals(message1, chatMessage.getMessage());
        assertEquals(ownMessage1, chatMessage.isOwnMessage());

        chatMessage.setSenderName(senderName2);
        chatMessage.setSciper(sciper2);
        chatMessage.setMessage(message2);
        chatMessage.setOwnMessage(ownMessage2);

        assertEquals(senderName2, chatMessage.getSenderName());
        assertEquals(sciper2, chatMessage.getSciper());
        assertEquals(message2, chatMessage.getMessage());
        assertEquals(ownMessage2, chatMessage.isOwnMessage());
    }

}
