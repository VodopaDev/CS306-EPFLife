package ch.epfl.sweng.zuluzulu.Fragments;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;

import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedAndFragment;
import ch.epfl.sweng.zuluzulu.Utility;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class EventDetailFragmentTest extends TestWithAuthenticatedAndFragment<EventDetailFragment> {

    @Rule
    public final IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule<>(MainActivity.class);

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());
        fragment = EventDetailFragment.newInstance(user, Utility.defaultEvent());
    }

    @Test
    public void isInEventDetailFragment() {
        onView(withId(R.id.event_detail_desc)).check(matches(isDisplayed()));
    }

    @Test
    public void canFollowAndUnfollow() {
        assertTrue(user.isFollowedEvent("0"));
        onView(withId(R.id.event_detail_like_button)).perform(ViewActions.click());
        assertFalse(user.isFollowedEvent("0"));
        onView(withId(R.id.event_detail_like_button)).perform(ViewActions.click());
        assertTrue(user.isFollowedEvent("0"));
    }

    @Test
    public void canExportEvent() {
        Intent resultData = new Intent();
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(anyIntent()).respondWith(result);
        onView(withId(R.id.event_detail_export)).perform(ViewActions.click());
    }

    @Test
    public void canAccessChannel() {
        onView(withId(R.id.event_detail_chatRoom)).perform(ViewActions.click());
        onView(withId(R.id.chat_posts_buttons)).check(matches(isDisplayed()));
    }

    @Test
    public void canAccessAssociation() {
        onView(withId(R.id.event_detail_but_assos)).perform(ViewActions.click());
        onView(withId(R.id.association_detail_icon)).check(matches(isDisplayed()));
    }

}
