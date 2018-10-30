package ch.epfl.sweng.zuluzulu;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


/**
 * This class test the MainActivity as a Guest User
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityAsGuestTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    private MainActivity mActivity;

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
    }

    @Test
    public void isAuthenticated() {
        // check not authenticated
        assertFalse(mActivity.isAuthenticated());
    }

    @Test
    public void getUser() {
        assertNotNull(mActivity.getUser());
    }
}