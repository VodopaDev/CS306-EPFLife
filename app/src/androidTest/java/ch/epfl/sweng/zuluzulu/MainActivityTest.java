package ch.epfl.sweng.zuluzulu;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private MainActivity mActivity;


    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);


    @Before
    public void setUp() throws Exception {
        mActivity = mActivityRule.getActivity();
    }

    @Test
    public void isAuthenticated() {
        assertFalse(mActivity.isAuthenticated());
        // Check it means it's not an user

        Utility.fullLogin();
        assertTrue(mActivity.isAuthenticated());
    }

    @Test
    public void getUser() {
        assertNotNull(mActivity.getUser());
    }
}