package ch.epfl.sweng.zuluzulu;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.Post;
import Utils;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;

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
    private static List<String> upScipers1;
    private static List<String> downScipers1;
    private static final String senderName2 = "";
    private static final String sciper2 = "222222";
    private static final String message2 = "Bond's message";
    private static final String color2 = "#f8b30f";
    private static final Long nbUps2 = 5L;
    private static final Long nbResponses2 = 5L;
    private static List<String> upScipers2;
    private static List<String> downScipers2;

    private static AuthenticatedUser user;
    private static Channel channel;
    private static Map data1 = new HashMap();
    private static Map data2 = new HashMap();
    private Post post1;
    private Post post2;

    @Before
    public void init() {
        user = Utility.createTestUser();
        channel = Utility.defaultChannel();

        upScipers1 = new ArrayList<>(Arrays.asList(user.getSciper()));
        downScipers1 = new ArrayList<>();
        upScipers2 = new ArrayList<>();
        downScipers2 = new ArrayList<>(Arrays.asList(user.getSciper()));

        data1.put("senderName", senderName1);
        data1.put("sciper", sciper1);
        data1.put("message", message1);
        data1.put("time", time);
        data1.put("color", color1);
        data1.put("nbUps", nbUps1);
        data1.put("nbResponses", nbResponses1);
        data1.put("upScipers", upScipers1);
        data1.put("downScipers", new ArrayList<>());

        data2.put("senderName", senderName2);
        data2.put("sciper", sciper2);
        data2.put("message", message2);
        data2.put("time", time);
        data2.put("color", color2);
        data2.put("nbUps", nbUps2);
        data2.put("nbResponses", nbResponses2);
        data2.put("upScipers", new ArrayList<>());
        data2.put("downScipers", new ArrayList<>(Arrays.asList(user.getSciper())));

        post1 = new Post(new FirebaseMapDecorator(data1), user.getSciper(), channel.getId());
        post2 = new Post(new FirebaseMapDecorator(data2), user.getSciper(), channel.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void testIncorrectDataConstructor() {
        data1.put("downScipers", upScipers1);
        new Post(new FirebaseMapDecorator(data1), user.getSciper(), channel.getId());
    }

    @Test
    public void testGetters() {
        assertEquals(senderName1, post1.getSenderName());
        assertEquals(sciper1, post1.getSenderSciper());
        assertEquals(message1, post1.getMessage());
        assertEquals(time, post1.getTime());
        assertEquals(color1, post1.getColor());
        assertEquals(nbUps1.intValue(), post1.getNbUps());
        assertEquals(nbResponses1.intValue(), post1.getNbResponses());
        assertEquals(channel.getId(), post1.getChannelId());
        assertEquals(user.getSciper(), post1.getUserSciper());
        assertEquals(upScipers1, post1.getUpScipers());
        assertEquals(downScipers1, post1.getDownScipers());
        assertEquals(null, post1.getId());
    }

    @Test
    public void testIsAnonymous() {
        assertFalse(post1.isAnonymous());
        assertTrue(post2.isAnonymous());
    }

    @Test
    public void testUpDownByUser() {
        assertTrue(post1.isUpByUser());
        assertFalse(post1.isDownByUser());
        post1.setUpByUser(false);
        assertFalse(post1.isUpByUser());

        assertFalse(post2.isUpByUser());
        assertTrue(post2.isDownByUser());
        post2.setDownByUser(false);
        assertFalse(post2.isDownByUser());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpButAlreadyDownRaisesException() {
        post1.setDownByUser(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDownButAlreadyUpRaisesException() {
        post2.setUpByUser(true);
    }
}
