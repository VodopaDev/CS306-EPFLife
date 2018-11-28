package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.Database.FirebaseMock;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Post;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedAndFragment;
import ch.epfl.sweng.zuluzulu.Utility;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class ReplyFragmentTest extends TestWithAuthenticatedAndFragment<ReplyFragment> {

    @Override
    public void initFragment() {
        fragment = ReplyFragment.newInstance(user, Utility.defaultPost());
        DatabaseFactory.setDependency(new FirebaseMock());
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
        onView(withId(R.id.reply_send_button)).check(matches(isEnabled()));
        onView(withId(R.id.reply_send_button)).perform(click());
    }

    @Test
    public void testUserCanSwipeDown() {
        onView(withId(R.id.reply_list_view)).perform(swipeDown());
    }

    @Test
    public void testUserCanUpOriginalPost() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        onData(instanceOf(Post.class)).inAdapterView(withId(R.id.reply_list_view)).atPosition(0).onChildView(withId(R.id.post_up_button)).check(matches(isDisplayed()));
        onData(instanceOf(Post.class)).inAdapterView(withId(R.id.reply_list_view)).atPosition(0).onChildView(withId(R.id.post_up_button)).perform(click());
    }

    @Test
    public void testUserCanDownOriginalPost() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        onData(instanceOf(Post.class)).inAdapterView(withId(R.id.reply_list_view)).atPosition(0).onChildView(withId(R.id.post_down_button)).perform(click());
    }
}
