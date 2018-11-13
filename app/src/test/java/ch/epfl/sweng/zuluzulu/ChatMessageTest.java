package ch.epfl.sweng.zuluzulu;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.Structure.ChatMessage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ChatMessageTest {

    private static final String senderName1 = "James";
    private static final String sciper1 = "111111";
    private static final String message1 = "James message";
    private static final Date time = new Date();
    private static final String senderName2 = "";
    private static final String sciper2 = "222222";
    private static final String message2 = "Bond's message";
    private static final String userSciper = sciper1;
    private static Map data1 = new HashMap();
    private static Map data2 = new HashMap();
    private ChatMessage chatMessage1;
    private ChatMessage chatMessage2;

    @Before
    public void init() {
        data1.put("senderName", senderName1);
        data1.put("sciper", sciper1);
        data1.put("message", message1);
        data1.put("time", time);

        data2.put("senderName", senderName2);
        data2.put("sciper", sciper2);
        data2.put("message", message2);
        data2.put("time", time);

        chatMessage1 = new ChatMessage(new FirebaseMapDecorator(data1), userSciper);
        chatMessage2 = new ChatMessage(new FirebaseMapDecorator(data2), userSciper);
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(senderName1, chatMessage1.getSenderName());
        assertEquals(sciper1, chatMessage1.getSciper());
        assertEquals(message1, chatMessage1.getMessage());
        assertEquals(time, chatMessage1.getTime());

        chatMessage1.setSenderName(senderName2);
        chatMessage1.setSciper(sciper2);
        chatMessage1.setMessage(message2);
        chatMessage1.setTime(time);

        assertEquals(senderName2, chatMessage1.getSenderName());
        assertEquals(sciper2, chatMessage1.getSciper());
        assertEquals(message2, chatMessage1.getMessage());
        assertEquals(time, chatMessage1.getTime());
    }

    @Test
    public void testIsOwnMessage() {
        assertTrue(chatMessage1.isOwnMessage());
        assertFalse(chatMessage2.isOwnMessage());
    }

    @Test
    public void testIsAnonymous() {
        assertTrue(chatMessage2.isAnonymous());
    }

}
