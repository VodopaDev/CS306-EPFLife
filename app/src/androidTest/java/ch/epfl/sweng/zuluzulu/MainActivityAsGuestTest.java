package ch.epfl.sweng.zuluzulu;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Fragments.MainFragment;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithGuestAndFragment;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


/**
 * This class test the MainActivity as a Guest User
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityAsGuestTest extends TestWithGuestAndFragment<MainFragment> {

    @Override
    public void initFragment() {
        fragment = MainFragment.newInstance(user);
    }

    @Test
    public void isAuthenticated() {
        // check not authenticated
        assertFalse(getMainActivity().isAuthenticated());
    }

    @Test
    public void getUser() {
        assertNotNull(getMainActivity().getUser());
    }

}