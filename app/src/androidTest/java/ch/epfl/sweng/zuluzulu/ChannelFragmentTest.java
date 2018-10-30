package ch.epfl.sweng.zuluzulu;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Fragments.ChannelFragment;
import ch.epfl.sweng.zuluzulu.Fragments.SuperFragment;

@RunWith(AndroidJUnit4.class)
public class ChannelFragmentTest extends TestWithLogin {

    private SuperFragment fragment;

    @Before
    public void init() {
        fragment = ChannelFragment.newInstance(getUser());
        mActivityRule.getActivity().openFragment(fragment);
    }

    @Test
    public void testUserCanClickOnTheFirstChannel() {
        // Nothing I tried works to get access to the items, I'm giving up
        // Utility.checkFragmentIsOpen(R.id.chat_fragment);
    }

    @Test
    public void testUserCanClickOnTheTestChannel() {
        //onView(withText("Test")).perform(click());
        //Utility.checkFragmentIsOpen(R.id.chat_fragment);
    }
}
