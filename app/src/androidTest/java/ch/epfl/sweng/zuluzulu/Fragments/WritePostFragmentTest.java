package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedAndFragment;
import ch.epfl.sweng.zuluzulu.Utility;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class WritePostFragmentTest extends TestWithAuthenticatedAndFragment<WritePostFragment> {

    @Override
    public void initFragment() {
        fragment = WritePostFragment.newInstance(user, Utility.defaultChannel());
    }

    @Test
    public void testSendButtonIsNotEnabledAtBeginning() {
        onView(withId(R.id.write_post_send_button)).check(matches(not(isEnabled())));
    }

    @Test
    public void testUserCanWrite() {
        onView(withId(R.id.write_post_textEdit)).perform(ViewActions.typeText("test")).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.write_post_send_button)).check(matches(isEnabled()));
        onView(withId(R.id.write_post_textEdit)).perform(ViewActions.clearText()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.write_post_send_button)).check(matches(not(isEnabled())));
    }

    @Test
    public void testChangeColor() {
        onView(withId(R.id.write_post_layout)).perform(ViewActions.click());
    }
}
