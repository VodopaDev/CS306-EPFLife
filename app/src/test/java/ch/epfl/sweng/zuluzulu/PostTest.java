package ch.epfl.sweng.zuluzulu;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.Structure.Post;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class PostTest {

    private static final String senderName1 = "James";
    private static final String sciper1 = "111111";
    private static final String message1 = "James message";
    private static final Date time = new Date();
    private static final String senderName2 = "";
    private static final String sciper2 = "222222";
    private static final String message2 = "Bond's message";
    private static Map data1 = new HashMap();
    private static Map data2 = new HashMap();
    private Post post1;
    private Post post2;

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

        post1 = new Post(new FirebaseMapDecorator(data1));
        post2 = new Post(new FirebaseMapDecorator(data2));
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(senderName1, post1.getSenderName());
        assertEquals(sciper1, post1.getSciper());
        assertEquals(message1, post1.getMessage());
        assertEquals(time, post1.getTime());

        post1.setSenderName(senderName2);
        post1.setSciper(sciper2);
        post1.setMessage(message2);
        post1.setTime(time);

        assertEquals(senderName2, post1.getSenderName());
        assertEquals(sciper2, post1.getSciper());
        assertEquals(message2, post1.getMessage());
        assertEquals(time, post1.getTime());
    }

    @Test
    public void testIsAnonym() {
        assertTrue(post2.isAnonym());
    }
}
