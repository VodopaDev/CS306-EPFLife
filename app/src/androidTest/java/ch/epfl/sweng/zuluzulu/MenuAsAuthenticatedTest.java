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
public class MenuAsAuthenticatedTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void openDrawer() {
        // Open the menu
        Utility.openMenu();

        // Navigate to the login
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_login));

        // Login
        Utility.login();


        // Open the menu again
        Utility.openMenu();
    }

    @Test
    public void testUserCanOpenFriendsFragment() {
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_friends));
    }

    @Test
    public void testUserCanOpenChatFragment() {
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_chat));
    }

    @Test
    public void testUserCanOpenLogoutFragment() {
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_logout));
    }
}
