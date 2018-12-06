package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;

import ch.epfl.sweng.zuluzulu.Fragments.EventFragment;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedAndFragment;
import ch.epfl.sweng.zuluzulu.Utility;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
public class EventDetailFragmentTest extends TestWithAuthenticatedAndFragment<EventFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());
        fragment = EventFragment.newInstance(user);
    }

    @Test
    public void authenticatedCanOpenAnEvent() {
        onData(anything()).inAdapterView(ViewMatchers.withId(R.id.event_fragment_listview)).atPosition(0).perform(click());
        onView(withId(R.id.event_detail_fav)).check(matches(isDisplayed()));
    }

    @Test
    public void authenticatedCanOpenTheChatOfAnEvent() {
        onData(anything()).inAdapterView(withId(R.id.event_fragment_listview)).atPosition(0).perform(click());
        onView(withId(R.id.event_detail_chatRoom)).perform(click());
        Utility.checkFragmentIsOpen(R.id.chat_fragment);
    }

    @Test
    public void authenticatedCanOpenTheAssociationOfAnEvent() {
        onData(anything()).inAdapterView(withId(R.id.event_fragment_listview)).atPosition(0).perform(click());
        onView(withId(R.id.event_detail_but_assos)).perform(click());
        Utility.checkFragmentIsOpen(R.id.association_detail_fragment);
    }
}
