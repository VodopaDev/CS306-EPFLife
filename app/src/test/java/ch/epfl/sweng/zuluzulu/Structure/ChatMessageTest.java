package ch.epfl.sweng.zuluzulu.Structure;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;
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

    private static final String id1 = "000";
    private static final String senderName1 = "James";
    private static final String sciper1 = "111111";
    private static final String message1 = "James message";

    private static final String id2 = "000";
    private static final String senderName2 = "";
    private static final String sciper2 = "222222";
    private static final String message2 = "Bond's message";

    private static final String channelId = "002";
    private static final Date time = new Date();

    private ChatMessage chatMessage1;
    private ChatMessage chatMessage2;

    @Before
    public void init() {
        chatMessage1 = new ChatMessage(
                id1,
                channelId,
                message1,
                time,
                senderName1,
                sciper1
        );

        chatMessage2 = new ChatMessage(
                id2,
                channelId,
                message2,
                time,
                senderName2,
                sciper2
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void incorrectMapThrowException(){
        new ChatMessage(new FirebaseMapDecorator(Collections.singletonMap("id","lol")));
    }

    @Test
    public void fmapConstructorMapTest(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("id", id1);
        map.put("channel_id", channelId);
        map.put("sender_sciper", sciper1);
        map.put("sender_name", senderName1);
        map.put("time", time);
        map.put("message", message1);

        assertEquals(map, new ChatMessage(new FirebaseMapDecorator(map)).getData());
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(id1, chatMessage1.getId());
        assertEquals(channelId, chatMessage1.getChannelId());
        assertEquals(senderName1, chatMessage1.getSenderName());
        assertEquals(sciper1, chatMessage1.getSenderSciper());
        assertEquals(message1, chatMessage1.getMessage());
        assertEquals(time, chatMessage1.getTime());
    }

    @Test
    public void testIsOwnMessage() {
        assertTrue(chatMessage1.isOwnMessage(sciper1));
        assertFalse(chatMessage2.isOwnMessage(sciper1));
    }

    @Test
    public void testIsAnonymous() {
        assertTrue(chatMessage2.isAnonymous());
        assertFalse(chatMessage1.isAnonymous());
    }

}
