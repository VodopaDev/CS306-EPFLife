package ch.epfl.sweng.zuluzulu.Firebase;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import ch.epfl.sweng.zuluzulu.Database.FirebaseMock;
import ch.epfl.sweng.zuluzulu.Firebase.Database.FirebaseFactory;
import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.Structure.ChatMessage;
import ch.epfl.sweng.zuluzulu.Utility;

import static org.junit.Assert.*;

public class FirebaseProxyTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    FirebaseProxy proxy;

    @Before
    public void setUp() throws Exception {
        FirebaseFactory.setDependency(new FirebaseMock());

        proxy = FirebaseProxy.getInstance();
    }

    @Test
    public void getAllAssociations() {
        proxy.getAllAssociations(x -> {});
    }

    @Test
    public void getAssociationFromId() {
        proxy.getAssociationFromId("1", x -> {});
    }

    @Test
    public void getAssociationsFromIds() {
        ArrayList<String> list = new ArrayList();
        list.add("1");
        proxy.getAssociationsFromIds(list, x -> {});
    }

    @Test
    public void addAssociation() {
        proxy.addAssociation(Utility.defaultAssociation());
    }

    @Test
    public void addEvent() {
        proxy.addEvent(Utility.defaultEvent());
    }

    @Test
    public void getAllEvents() {
        proxy.getAllEvents(x -> {});
    }

    @Test
    public void getEventFromId() {
        proxy.getEventFromId("1", x -> {});
    }

    @Test
    public void getEventsFromIds() {
        ArrayList<String> list = new ArrayList();
        list.add("1");
        proxy.getEventsFromIds(list, x -> {});
    }

    @Test
    public void getAllChannels() {
        proxy.getAllChannels(x -> {});
    }

    @Test
    public void getChannelFromId() {
        proxy.getChannelFromId("1", x -> {});
    }

    @Test
    public void getChannelsFromIds() {
        ArrayList<String> list = new ArrayList();
        list.add("1");
        proxy.getChannelsFromIds(list, x -> {});
    }

    @Test
    public void getMessagesFromChannel() {
        proxy.getMessagesFromChannel("1", x -> {});
    }

    @Test
    public void getPostsFromChannel() {
        proxy.getPostsFromChannel("1", x -> {});
    }

    @Test
    public void getRepliesFromPost() {
        proxy.getRepliesFromPost("1", "1", x -> {});
    }

    @Test
    public void updateOnNewMessagesFromChannel() {
        proxy.updateOnNewMessagesFromChannel("1", x -> {});
    }

    @Test
    public void addPost() {
        proxy.addPost(Utility.defaultPost());
    }

    @Test
    public void addReply() {
        proxy.addReply(Utility.defaultPost());
    }

    @Test
    public void updateUser() {
        proxy.updateUser(Utility.createTestAuthenticated());
    }

    @Test
    public void updatePost() {
        proxy.updatePost(Utility.defaultPost());
    }

    @Test
    public void addMessage() {
        proxy.addMessage(Utility.defaultMessage());
    }

    @Test
    public void updateUser1() {
        proxy.updateUser(Utility.createTestAuthenticated());
    }

    @Test
    public void getUserWithIdOrCreateIt() {
        proxy.getUserWithIdOrCreateIt("1", x -> {});
    }

    @Test
    public void getNewChannelId() {
        assertEquals("1", proxy.getNewChannelId());
    }

    @Test
    public void getNewEventId() {
        assertEquals("1", proxy.getNewEventId());
    }

    @Test
    public void getNewAssociationId() {
        assertEquals("1", proxy.getNewAssociationId());
    }

    @Test
    public void getNewPostId() {
        assertEquals("1", proxy.getNewPostId("1"));
    }


    @Test
    public void getNewMessageId() {
        assertEquals("1", proxy.getNewMessageId("1"));
    }

    @Test
    public void getNewReplyId() {
        assertEquals("1", proxy.getNewReplyId("1", "1"));
    }
}