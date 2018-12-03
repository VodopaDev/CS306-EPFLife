package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedAndFragment;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.instanceOf;

@RunWith(AndroidJUnit4.class)
public class ChannelFragmentTest extends TestWithAuthenticatedAndFragment<ChannelFragment> {
    
    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());
        fragment = ChannelFragment.newInstance(user);
    }

    @Test
    public void testUserCanClickOnChannels() {
        onView(ViewMatchers.withText("Testing channel")).perform(ViewActions.click());
    }

    @Test
    public void testUserCanSwipeUp() {
        onView(withId(R.id.channels_list_view)).perform(ViewActions.swipeDown());
    }

    @Test
    public void testUserCanOpenChannel() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        onData(instanceOf(Channel.class)).atPosition(0).check(matches(isDisplayed()));
        onData(instanceOf(Channel.class)).atPosition(0).perform(click());
        onView(withId(R.id.chat_list_view)).check(matches(isDisplayed()));
    }
}
