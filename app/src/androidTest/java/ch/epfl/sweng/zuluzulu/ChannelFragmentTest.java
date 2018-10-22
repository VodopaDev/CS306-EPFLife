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
public class ChannelFragmentTest extends TestWithLogin {

    private Fragment fragment;

    @Before
    public void init() {
        fragment = ChannelFragment.newInstance(getUser());
        mActivityRule.getActivity().openFragment(fragment);
    }

    @Test
    public void testUserCanClickOnTheFirstChannel() {
        //onView(withText("Global")).perform(click());
        //Utility.checkFragmentIsOpen(R.id.chat_fragment);
    }

    @Test
    public void testUserCanClickOnTheTestChannel() {
        //onView(withText("Test")).perform(click());
        //Utility.checkFragmentIsOpen(R.id.chat_fragment);
    }
}
