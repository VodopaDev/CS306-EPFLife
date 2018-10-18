package ch.epfl.sweng.zuluzulu;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Fragments.ChannelFragment;
import ch.epfl.sweng.zuluzulu.Structure.User;

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
    }

    @Test
    public void testUserCanClickOnTheFirstChannel() {
    }
}
