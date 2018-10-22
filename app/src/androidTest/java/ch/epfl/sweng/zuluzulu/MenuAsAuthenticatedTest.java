package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MenuAsAuthenticatedTest extends TestWithLogin{

    @Before
    public void openDrawer() {
        // Open the menu again
        Utility.openMenu();
    }

    @Test
    public void testUserCanOpenFriendsFragment() {
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_friends));


        // Check if it's open TODO when created
    }

    @Test
    public void testUserCanOpenChannelFragment() {
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_chat));


        // Check if it's open
        Utility.checkFragmentIsOpen(R.id.channel_fragment);
    }

    @Test
    public void testUserCanOpenProfileFragment() {
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_profile));


        // Check if it's open
        Utility.checkFragmentIsOpen(R.id.profile_fragment);
    }

    @Test
    public void testUserCanOpenLogoutFragment() {
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_logout));
    }
}
