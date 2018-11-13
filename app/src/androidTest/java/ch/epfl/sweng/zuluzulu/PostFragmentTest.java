package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Fragments.PostFragment;
import ch.epfl.sweng.zuluzulu.Fragments.SuperFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class PostFragmentTest extends TestWithAuthenticatedUser {

    @Before
    public void init() {
        SuperFragment fragment = PostFragment.newInstance(getUser(), Utility.defaultChannel());
        mActivityRule.getActivity().openFragment(fragment);
    }

    @Test
    public void testUserCanSeePosts() {
        onView(withId(R.id.posts_list_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testOnlyOneButtonIsEnabled() {
        onView(withId(R.id.chat_button)).check(matches(isEnabled()));
        onView(withId(R.id.posts_button)).check(matches(not(isEnabled())));
    }

    @Test
    public void testUserCanGoToChat() {
        onView(withId(R.id.chat_button)).perform(ViewActions.click());
        onView(withId(R.id.chat_list_view)).check(matches(isDisplayed()));
    }
}
