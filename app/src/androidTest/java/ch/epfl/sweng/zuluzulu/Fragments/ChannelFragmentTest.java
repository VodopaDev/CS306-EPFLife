package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Fragments.ChannelFragment;
import ch.epfl.sweng.zuluzulu.Fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ChannelFragmentTest extends TestWithAuthenticatedAndFragment<ChannelFragment> {

    @Override
    public void initFragment() {
        fragment = ChannelFragment.newInstance(user);
    }

    @Test
    public void testUserCanClickOnChannels() {
        onView(ViewMatchers.withId(R.id.channels_list_view)).perform(ViewActions.click());
    }

    @Test
    public void testUserCanSwipeUp() {
        onView(withId(R.id.channels_list_view)).perform(ViewActions.swipeUp());
    }

}
