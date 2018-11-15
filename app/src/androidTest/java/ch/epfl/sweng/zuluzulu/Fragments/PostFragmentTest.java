package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class PostFragmentTest extends TestWithAuthenticatedAndFragment<PostFragment> {

    @Override
    public void initFragment() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", 1L);
        data.put("name", "name");
        data.put("description", "description");
        data.put("restrictions", new HashMap<>());
        FirebaseMapDecorator fmap = new FirebaseMapDecorator(data);
        fragment = PostFragment.newInstance(user, new Channel(fmap));
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
}
