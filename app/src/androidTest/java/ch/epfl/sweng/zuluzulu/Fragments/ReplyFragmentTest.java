package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Database.FirebaseMock;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedAndFragment;
import ch.epfl.sweng.zuluzulu.Utility;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class ReplyFragmentTest extends TestWithAuthenticatedAndFragment<ReplyFragment> {

    @Override
    public void initFragment() {
        fragment = ReplyFragment.newInstance(user, Utility.defaultPost());
        DatabaseFactory.setDependency(new FirebaseMock());
    }

    @Test
    public void testUserCanSeeOriginalPost() {
        onView(withId(R.id.reply_original_post)).check(matches(isDisplayed()));
    }

    @Test
    public void testUserCanSeeReplies() {
        onView(withId(R.id.reply_list_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testUserCannotSendInvalidReply() {
        onView(withId(R.id.reply_send_button)).check(matches(not(isEnabled())));
        onView(withId(R.id.reply_text_edit)).perform(ViewActions.typeText("test")).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.reply_send_button)).check(matches(isEnabled()));
        onView(withId(R.id.reply_text_edit)).perform(ViewActions.clearText()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.reply_send_button)).check(matches(not(isEnabled())));
    }

    @Test
    public void testUserCanSendReply() {
        onView(withId(R.id.reply_text_edit)).perform(ViewActions.typeText("test")).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.reply_send_button)).perform(ViewActions.click());
    }

    @Test
    public void testUserCanUpOriginalPost() {
        // Todo
    }

    @Test
    public void testUserCanUpReply() {
        // Todo
    }
}
