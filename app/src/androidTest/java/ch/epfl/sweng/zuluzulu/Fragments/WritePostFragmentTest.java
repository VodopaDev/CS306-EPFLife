package ch.epfl.sweng.zuluzulu.Fragments;

import android.graphics.drawable.ColorDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Database.FirebaseMock;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedAndFragment;
import ch.epfl.sweng.zuluzulu.Utility;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class WritePostFragmentTest extends TestWithAuthenticatedAndFragment<WritePostFragment> {

    @Override
    public void initFragment() {
        fragment = WritePostFragment.newInstance(user, Utility.defaultChannel());
        DatabaseFactory.setDependency(new FirebaseMock());
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
        // int oldColor = getMainActivity().findViewById(R.id.write_post_layout).getSolidColor();
        onView(withId(R.id.write_post_layout)).perform(ViewActions.click());
        // onView(withId(R.id.write_post_layout)).check(matches(not(hasBackgroundColor(oldColor))));
    }

    @Test
    public void testUserCanSendPost() {
        onView(withId(R.id.write_post_textEdit)).perform(ViewActions.typeText("test")).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.write_post_send_button)).perform(ViewActions.click());
        onView(withId(R.id.posts_list_view)).check(matches(isDisplayed()));
    }

    /*
    public static Matcher<View> hasBackgroundColor(final int color) {
        return new BoundedMatcher<View, ConstraintLayout>(ConstraintLayout.class) {
            @Override
            public boolean matchesSafely(ConstraintLayout layout) {
                return color == layout.getSolidColor();
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("with background color: ");
            }
        };
    }
     */
}
