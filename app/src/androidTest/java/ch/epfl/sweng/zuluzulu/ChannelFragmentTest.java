package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Fragments.ChannelFragment;
import ch.epfl.sweng.zuluzulu.Fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedUser;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ChannelFragmentTest extends TestWithAuthenticatedUser {

    private SuperFragment fragment;

    @Before
    public void init() {
        fragment = ChannelFragment.newInstance(getUser());
        mActivityRule.getActivity().openFragment(fragment);
    }

    @Test
    public void testUserCanClickOnChannels() {
        onView(withId(R.id.channels_list_view)).perform(ViewActions.click());
    }

    @Test
    public void testUserCanSwipeUp() {
        onView(withId(R.id.channels_list_view)).perform(ViewActions.swipeUp());
    }
}
