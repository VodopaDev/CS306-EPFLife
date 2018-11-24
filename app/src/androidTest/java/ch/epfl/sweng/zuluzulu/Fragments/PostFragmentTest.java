package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class PostFragmentTest extends TestWithAuthenticatedAndFragment<PostFragment> {

    @Override
    public void initFragment() {
        fragment = PostFragment.newInstance(user, Utility.defaultChannel());
        DatabaseFactory.setDependency(new FirebaseMock());
    }

    @Test
    public void testUserCanSeePosts() {
        onView(ViewMatchers.withId(R.id.posts_list_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testChatButtonIsEnabled() {
        onView(withId(R.id.posts_button)).check(matches(not(isEnabled())));
        onView(withId(R.id.chat_button)).check(matches(isEnabled()));
    }

    @Test
    public void testUserCanGoToChat() {
        onView(withId(R.id.chat_button)).perform(ViewActions.click());
        onView(withId(R.id.chat_list_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testUserCanSwipeDown() {
        onView(withId(R.id.posts_list_view)).perform(ViewActions.swipeDown());
    }

    @Test
    public void testUserCanUpPost() throws InterruptedException {
        // Todo
        // TimeUnit.SECONDS.sleep(10);
        // onData(anything()).inAdapterView(withId(R.id.posts_list_view)).onChildView(withId(R.id.post_up_button)).check(matches(isDisplayed()));
    }
}
