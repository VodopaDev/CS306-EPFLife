package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.contrib.NavigationViewActions;

import org.junit.Test;

import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedUser;
import ch.epfl.sweng.zuluzulu.TestingUtility.Utility;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class LogoutTest extends TestWithAuthenticatedUser {
    @Test
    public void logout() {
        Utility.openMenu();

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_logout));

        Utility.checkFragmentIsOpen(R.id.main_fragment);
    }
}