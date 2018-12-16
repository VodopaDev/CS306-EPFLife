package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;

import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Post;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedAndFragment;
import ch.epfl.sweng.zuluzulu.Utility;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringStartsWith.startsWith;

@RunWith(AndroidJUnit4.class)
public class PostFragmentTest extends TestWithAuthenticatedAndFragment<PostFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());

        fragment = PostFragment.newInstance(user, Utility.defaultChannel());
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
        onView(withId(R.id.chat_button)).perform(click());
        onView(withId(R.id.chat_list_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testUserCanSwipeDown() {
        onView(withId(R.id.posts_list_view)).perform(ViewActions.swipeDown());
    }

    @Test
    public void testUserCanOpenWritePostFragment() {
        onView(withId(R.id.posts_new_post_button)).perform(click());
        onView(withId(R.id.write_post_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void upAndDownButtonAreVisible() {
        onData(instanceOf(Post.class)).atPosition(0).onChildView(withId(R.id.post_up_button)).check(matches(isDisplayed()));
        onData(instanceOf(Post.class)).atPosition(0).onChildView(withId(R.id.post_down_button)).check(matches(isDisplayed()));

    }

    @Test
    public void testUserCanOpenPost() {
        onData(instanceOf(Post.class)).atPosition(0).perform(click());
        onView(withId(R.id.reply_list_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testUserCanUpDownPost() {
        onData(instanceOf(Post.class)).atPosition(0).onChildView(withId(R.id.post_up_button)).check(matches(isDisplayed()));
        onData(instanceOf(Post.class)).atPosition(0).onChildView(withId(R.id.post_up_button)).perform(click());
    }

    @Test
    public void testUserCanDownPost() {
        onData(instanceOf(Post.class)).atPosition(0).onChildView(withId(R.id.post_down_button)).perform(click());
    }

    @Test
    public void testUserCanFilterPosts() {
        onView(withId(R.id.post_filter_time)).perform(click());
        onView(withId(R.id.post_filter_nbReplies)).perform(click());
        onView(withId(R.id.post_filter_nbUps)).perform(click());
    }

    @Test
    public void testUserCanReachOtherProfileThroughPost() {
        onView(withId(R.id.posts_list_view)).check(matches(isDisplayed()));
        onView(withId(R.id.posts_new_post_button)).check(matches(isDisplayed()));
        onData(instanceOf(Post.class)).atPosition(0).check(matches(isDisplayed()));
        onData(instanceOf(Post.class)).atPosition(0).perform(ViewActions.longClick());
        onView(withText(startsWith(SuperChatPostsFragment.VISIT_PROFILE_STRING))).check(matches(isDisplayed()));
        onView(withText("Oui")).perform(click());
        Utility.checkFragmentIsOpen(R.id.profile_fragment);
    }
}
