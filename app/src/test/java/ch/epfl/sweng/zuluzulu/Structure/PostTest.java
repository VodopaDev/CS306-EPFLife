package ch.epfl.sweng.zuluzulu.Structure;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.Post;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class PostTest {

    private static final String currentUserId = "000";
    private static final String channelId = "001";

    private static final String id1 = "1";
    private static final String originalPostId1 = "002";
    private static final String senderName1 = "James";
    private static final String sciper1 = "111111";
    private static final String message1 = "James message";
    private static final String color1 = "#e8b30f";
    private static final Date time = new Date();
    private static final int nbUps1 = 1;
    private static final int nbResponses1 = 0;
    private static List<String> upScipers1 = Collections.singletonList(currentUserId);
    private static List<String> downScipers1 = Collections.emptyList();

    private static final String id2 = "2";
    private static final String originalPostId2 = null;
    private static final String senderName2 = "";
    private static final String sciper2 = "222222";
    private static final String message2 = "Bond's message";
    private static final String color2 = "#f8b30f";
    private static final int nbUps2 = -1;
    private static final int nbResponses2 = 5;
    private static List<String> upScipers2 = Collections.emptyList();
    private static List<String> downScipers2 = Collections.singletonList(currentUserId);

    private Post post1;
    private Post post2;

    @Before
    public void init() {
        post1 = new Post(id1,
                channelId,
                originalPostId1,
                message1,
                senderName1,
                sciper1,
                time,
                color1,
                nbResponses1,
                nbUps1,
                upScipers1,
                downScipers1
        );
        post2 = new Post(id2,
                channelId,
                originalPostId2,
                message2,
                senderName2,
                sciper2,
                time,
                color2,
                nbResponses2,
                nbUps2,
                upScipers2,
                downScipers2
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectDataConstructor() {
        new Post(new FirebaseMapDecorator(Collections.singletonMap("id", "lol")));
    }

    @Test
    public void testGetters() {
        assertEquals(senderName1, post1.getSenderName());
        assertEquals(sciper1, post1.getSenderSciper());
        assertEquals(message1, post1.getMessage());
        assertEquals(time, post1.getTime());
        assertEquals(color1, post1.getColor());
        assertEquals(nbUps1, post1.getNbUps());
        assertEquals(nbResponses1, post1.getNbResponses());
        assertEquals(channelId, post1.getChannelId());
        assertEquals(upScipers1, post1.getUpScipers());
        assertEquals(downScipers1, post1.getDownScipers());
        assertEquals(id1, post1.getId());
    }

    @Test
    public void testIsAnonymous() {
        assertFalse(post1.isAnonymous());
        assertTrue(post2.isAnonymous());
    }
}
