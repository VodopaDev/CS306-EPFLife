package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Fragments.AssociationFragment;
import ch.epfl.sweng.zuluzulu.Fragments.MainFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
/**
 * This class test the MainActivity as a Guest User
 */
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
    public void currentFragmentIsCorrect() {
        assertTrue(mActivity.getCurrentFragment() instanceof MainFragment);
        Utility.openMenu();
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_associations));
        assertTrue(mActivity.getCurrentFragment() instanceof AssociationFragment);
        Espresso.pressBack();
        assertTrue(mActivity.getCurrentFragment() instanceof MainFragment);
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