package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;
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
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class WritePostFragmentTest extends TestWithAuthenticatedAndFragment<WritePostFragment>{

    @Override
    public void initFragment() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", 1L);
        data.put("name", "name");
        data.put("description", "description");
        data.put("restrictions", new HashMap<>());
        FirebaseMapDecorator fmap = new FirebaseMapDecorator(data);
        fragment = WritePostFragment.newInstance(user, new Channel(fmap));
    }

    @Test
    public void testSendButtonIsNotEnabled() {
        onView(withId(R.id.write_post_send_button)).check(matches(not(isEnabled())));
    }

    @Test
    public void testUserCanWrite() {
        onView(withId(R.id.write_post_textEdit)).perform(ViewActions.typeText("test")).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.write_post_send_button)).check(matches(isEnabled()));
        onView(withId(R.id.write_post_textEdit)).perform(ViewActions.clearText());
        testSendButtonIsNotEnabled();
    }

}
