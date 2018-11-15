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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class PostTest {

    private static final String senderName1 = "James";
    private static final String sciper1 = "111111";
    private static final String message1 = "James message";
    private static final String color1 = "#e8b30f";
    private static final Date time = new Date();
    private static final Long nbUps1 = 0L;
    private static final Long nbResponses1 = 0L;
    private static final String senderName2 = "";
    private static final String sciper2 = "222222";
    private static final String message2 = "Bond's message";
    private static final String color2 = "#f8b30f";
    private static final Long nbUps2 = 5L;
    private static final Long nbResponses2 = 5L;
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
        data1.put("color", color1);
        data1.put("nbUps", nbUps1);
        data1.put("nbResponses", nbResponses1);

        data2.put("senderName", senderName2);
        data2.put("sciper", sciper2);
        data2.put("message", message2);
        data2.put("time", time);
        data2.put("color", color2);
        data2.put("nbUps", nbUps2);
        data2.put("nbResponses", nbResponses2);

        post1 = new Post(new FirebaseMapDecorator(data1));
        post2 = new Post(new FirebaseMapDecorator(data2));
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(senderName1, post1.getSenderName());
        assertEquals(sciper1, post1.getSciper());
        assertEquals(message1, post1.getMessage());
        assertEquals(time, post1.getTime());
        assertEquals(color1, post1.getColor());
        assertEquals(nbUps1.intValue(), post1.getNbUps());
        assertEquals(nbResponses1.intValue(), post1.getNbResponses());

        post1.setSenderName(senderName2);
        post1.setSciper(sciper2);
        post1.setMessage(message2);
        post1.setTime(time);
        post1.setColor(color2);
        post1.setNbUps(nbUps2.intValue());
        post1.setNbResponses(nbResponses2.intValue());

        assertEquals(senderName2, post1.getSenderName());
        assertEquals(sciper2, post1.getSciper());
        assertEquals(message2, post1.getMessage());
        assertEquals(time, post1.getTime());
        assertEquals(color2, post1.getColor());
        assertEquals(nbUps2.intValue(), post1.getNbUps());
        assertEquals(nbResponses2.intValue(), post1.getNbResponses());
    }

    @Test
    public void testIsAnonymous() {
        assertFalse(post1.isAnonymous());
        assertTrue(post2.isAnonymous());
    }
}
