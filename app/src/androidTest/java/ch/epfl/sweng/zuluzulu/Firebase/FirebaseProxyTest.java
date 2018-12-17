package ch.epfl.sweng.zuluzulu.Firebase;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.IdlingResource.IdlingResourceFactory;
import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.Utility;

import static org.junit.Assert.*;

public class FirebaseProxyTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);


    @BeforeClass
    static public void setUp() {
        DatabaseFactory.setDependency(new MockedProxy());
        IdlingRegistry.getInstance().register(IdlingResourceFactory.getCountingIdlingResource());
    }

    @Test
    public void getAllAssociations() {
        FirebaseProxy.getInstance().getAllAssociations(x -> {
        });
    }

    @Test
    public void getAssociationFromId() {
        FirebaseProxy.getInstance().getAssociationFromId("1", x -> {
        });
    }

    @Test
    public void getAssociationsFromIds() {
        ArrayList<String> list = new ArrayList();
        list.add("1");
        FirebaseProxy.getInstance().getAssociationsFromIds(list, x -> {
        });
    }

    @Test
    public void addAssociation() {
        FirebaseProxy.getInstance().addAssociation(Utility.defaultAssociation());
    }

    @Test
    public void addEvent() {
        FirebaseProxy.getInstance().addEvent(Utility.defaultEvent().build());
    }

    @Test
    public void getAllEvents() {
        FirebaseProxy.getInstance().getAllEvents(x -> {
        });
    }

    @Test
    public void getEventFromId() {
        FirebaseProxy.getInstance().getEventFromId("1", x -> {
        });
    }

    @Test
    public void getEventsFromIds() {
        ArrayList<String> list = new ArrayList();
        list.add("1");
        FirebaseProxy.getInstance().getEventsFromIds(list, x -> {
        });
    }

    @Test
    public void getAllChannels() {
        FirebaseProxy.getInstance().getAllChannels(x -> {
        });
    }

    @Test
    public void getChannelFromId() {
        FirebaseProxy.getInstance().getChannelFromId("1", x -> {
        });
    }

    @Test
    public void getChannelsFromIds() {
        ArrayList<String> list = new ArrayList();
        list.add("1");
        FirebaseProxy.getInstance().getChannelsFromIds(list, x -> {
        });
    }

    @Test
    public void getMessagesFromChannel() {
        FirebaseProxy.getInstance().getMessagesFromChannel("1", x -> {
        });
    }

    @Test
    public void getPostsFromChannel() {
        FirebaseProxy.getInstance().getPostsFromChannel("1", x -> {
        });
    }

    @Test
    public void addChannelToUserFollowedChannels() {
        FirebaseProxy.getInstance().addChannelToUserFollowedChannels(Utility.defaultChannel(), Utility.createTestAuthenticated());
    }

    @Test
    public void addEventToUserFollowedEvents() {
        FirebaseProxy.getInstance().addEventToUserFollowedEvents(Utility.defaultEvent().build(), Utility.createTestAuthenticated());
    }

    @Test
    public void addAssociationToUserFollowedAssociations() {
        FirebaseProxy.getInstance().addAssociationToUserFollowedAssociations(Utility.defaultAssociation(), Utility.createTestAuthenticated());
    }

    @Test
    public void removeChannelFromUserFollowedChannels() {
        FirebaseProxy.getInstance().removeChannelFromUserFollowedChannels(Utility.defaultChannel(), Utility.createTestAuthenticated());
    }

    @Test
    public void removeEventFromUserFollowedEvents() {
        FirebaseProxy.getInstance().removeEventFromUserFollowedEvents(Utility.defaultEvent().build(), Utility.createTestAuthenticated());
    }

    @Test
    public void removeAssociationFromUserFollowedAssociations() {
        FirebaseProxy.getInstance().removeAssociationFromUserFollowedAssociations(Utility.defaultAssociation(), Utility.createTestAuthenticated());
    }

    @Test
    public void getRepliesFromPost() {
        FirebaseProxy.getInstance().getRepliesFromPost("1", "1", x -> {
        });
    }

    @Test
    public void updateOnNewMessagesFromChannel() {
        FirebaseProxy.getInstance().updateOnNewMessagesFromChannel("1", x -> {
        });
    }

    @Test
    public void addPost() {
        FirebaseProxy.getInstance().addPost(Utility.defaultPost0());
    }

    @Test
    public void addReply() {
        FirebaseProxy.getInstance().addReply(Utility.defaultPost0());
    }

    @Test
    public void updateUser() {
        FirebaseProxy.getInstance().updateUser(Utility.createTestAuthenticated());
    }

    @Test
    public void updatePost() {
        FirebaseProxy.getInstance().updatePost(Utility.defaultPost0());
    }

    @Test
    public void addMessage() {
        FirebaseProxy.getInstance().addMessage(Utility.defaultMessage0());
    }

    @Test
    public void updateUser1() {
        FirebaseProxy.getInstance().updateUser(Utility.createTestAuthenticated());
    }

    @Test
    public void getAllUsers() {
        FirebaseProxy.getInstance().getAllUsers(x -> {
        });
    }

    @Test
    public void updateUserRole() {
        FirebaseProxy.getInstance().updateUserRole("0", new ArrayList<>());
    }

    @Test
    public void getUserWithIdOrCreateIt() {
        FirebaseProxy.getInstance().getUserWithIdOrCreateIt("1", x -> {
        });
    }

    @Test
    public void getNewChannelId() {
        assertEquals("1", FirebaseProxy.getInstance().getNewChannelId());
    }

    @Test
    public void getNewEventId() {
        assertEquals("1", FirebaseProxy.getInstance().getNewEventId());
    }

    @Test
    public void getNewAssociationId() {
        assertEquals("1", FirebaseProxy.getInstance().getNewAssociationId());
    }

    @Test
    public void getNewPostId() {
        assertEquals("1", FirebaseProxy.getInstance().getNewPostId("1"));
    }


    @Test
    public void getNewMessageId() {
        assertEquals("1", FirebaseProxy.getInstance().getNewMessageId("1"));
    }

    @Test
    public void getNewReplyId() {
        assertEquals("1", FirebaseProxy.getInstance().getNewReplyId("1", "1"));
    }
}