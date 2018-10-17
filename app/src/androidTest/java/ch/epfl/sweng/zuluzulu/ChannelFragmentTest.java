package ch.epfl.sweng.zuluzulu;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.Fragments.ChannelFragment;
import ch.epfl.sweng.zuluzulu.Structure.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ChannelFragmentTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);
    private User user;
    private Fragment fragment;

    @Before
    public void init() throws InterruptedException {
        user = Utility.createTestUser();

        Utility.addUserToMainIntent(mActivityRule, user);

        fragment = ChannelFragment.newInstance(user);
        mActivityRule.getActivity().openFragment(fragment);

        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void testUserCanClickOnTheFirstChannel() {
        onView(withText("Global")).perform(click());
        Utility.checkFragmentIsOpen(R.id.chat_fragment);
    }

    @Test
    public void testUserCanClickOnTheTestChannel() {
        onView(withText("Test")).perform(click());
        Utility.checkFragmentIsOpen(R.id.chat_fragment);
    }
}
