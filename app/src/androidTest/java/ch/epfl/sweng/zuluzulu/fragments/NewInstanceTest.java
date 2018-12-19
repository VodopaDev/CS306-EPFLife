package ch.epfl.sweng.zuluzulu.fragments;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Utility;
import ch.epfl.sweng.zuluzulu.fragments.superFragments.SuperChatPostsFragment;

@RunWith(AndroidJUnit4.class)
public class NewInstanceTest {

    @Test(expected = IllegalArgumentException.class)
    public void nullUserForAssociationDetail(){
        AssociationDetailFragment.newInstance(null, Utility.defaultAssociation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullAssociationForAssociationDetail(){
        AssociationDetailFragment.newInstance(Utility.createTestGuest(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUserForAssociation(){
        AssociationFragment.newInstance(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUserForCalendar(){
        CalendarFragment.newInstance(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUserForChannel(){
        ChannelFragment.newInstance(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUserForChat(){
        ChatFragment.newInstance(null, Utility.defaultChannel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullChannelForChat(){
        ChatFragment.newInstance(Utility.createTestAdmin(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUserForEventDetail(){
        EventDetailFragment.newInstance(null, Utility.defaultEvent().build());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullEventForEventDetail(){
        EventDetailFragment.newInstance(Utility.createTestGuest(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUserForEvent(){
        AssociationFragment.newInstance(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUserForMain(){
        MainFragment.newInstance(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUserForPost(){
        PostFragment.newInstance(null, Utility.defaultChannel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullChannelForPost(){
        PostFragment.newInstance(Utility.createTestAdmin(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUserForProfile(){
        ProfileFragment.newInstance(null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUserForReply(){
        ReplyFragment.newInstance(null, Utility.defaultChannel(), Utility.defaultPost0());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullChannelForReply(){
        ReplyFragment.newInstance(Utility.createTestAdmin(), null, Utility.defaultPost0());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullPostForReply(){
        ReplyFragment.newInstance(Utility.createTestAdmin(), Utility.defaultChannel(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullTypeForSuperChatPost(){
        SuperChatPostsFragment.newInstanceOf(null, Utility.createTestAdmin(), Utility.defaultChannel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUserForSuperChatPost(){
        SuperChatPostsFragment.newInstanceOf("lol", null, Utility.defaultChannel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullChannelForSuperChatPost(){
        SuperChatPostsFragment.newInstanceOf("lol", Utility.createTestAdmin(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUserForWritePost(){
        WritePostFragment.newInstance(null, Utility.defaultChannel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullChannelForWritePost(){
        WritePostFragment.newInstance(Utility.createTestAdmin(), null);
    }

}
