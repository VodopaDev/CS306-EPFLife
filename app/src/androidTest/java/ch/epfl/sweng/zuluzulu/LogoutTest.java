package ch.epfl.sweng.zuluzulu;


import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LogoutTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);


    @Before
    public void logIn() {
        openLoginFragment();

        // Log on
        onView(withId(R.id.username)).perform(typeText("user")).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("password")).perform(closeSoftKeyboard());
        onView(withId(R.id.sign_in_button)).perform(click());
    }

    @Test
    public void Logout() {
        openDrawer();
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_logout));

        openLoginFragment();
    }

    /**
     * Open the drawer
     */
    private void openDrawer(){
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

    }

    /**
     * Open the login fragment in the drawer
     */
    private void openLoginFragment() {
        openDrawer();

        // Click on the login item in the Drawer
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_login));
    }

}
