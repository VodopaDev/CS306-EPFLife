package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;

import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedAndFragment;
import ch.epfl.sweng.zuluzulu.Utility;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class EventDetailFragmentTest extends TestWithAuthenticatedAndFragment<EventDetailFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());
        fragment = EventDetailFragment.newInstance(user, Utility.defaultEvent());
    }

    @Test
    public void isInEventDetailFragment(){
        onView(withId(R.id.event_detail_desc)).check(matches(isDisplayed()));
    }

    @Test
    public void canFollowAndUnfollow(){
        assertTrue(user.isFollowedEvent("1"));
        onView(withId(R.id.event_detail_like))
                .check(matches(isClickable()))
                .perform(ViewActions.click());
        assertFalse(user.isFollowedEvent("1"));
        onView(withId(R.id.event_detail_like)).perform(ViewActions.click());
        assertTrue(user.isFollowedEvent("1"));
    }

    @Test
    public void canExportEvent(){
        onView(withId(R.id.event_detail_export)).perform(ViewActions.click());
    }

    @Test
    public void canAccessChannel(){
        onView(withId(R.id.event_detail_channel)).perform(ViewActions.click());
        onView(withId(R.id.chat_posts_buttons)).check(matches(isDisplayed()));
    }

    @Test
    public void canAccessAssociation(){
        onView(withId(R.id.event_detail_association)).perform(ViewActions.click());
        onView(withId(R.id.association_detail_icon)).check(matches(isDisplayed()));
    }

}
