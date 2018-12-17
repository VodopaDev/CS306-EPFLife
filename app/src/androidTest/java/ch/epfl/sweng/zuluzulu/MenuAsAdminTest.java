package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.contrib.NavigationViewActions;

import org.junit.Test;

import ch.epfl.sweng.zuluzulu.database.MockedProxy;
import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.fragments.MainFragment;
import ch.epfl.sweng.zuluzulu.testingUtility.TestWithAdminAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class MenuAsAdminTest extends TestWithAdminAndFragment<MainFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());
        fragment = MainFragment.newInstance(user);
    }

    @Test
    public void testCanOpenAdminPanel() {
        Utility.openMenu();
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_admin_panel));
        onView(withId(R.id.panel_channel)).check(matches(isDisplayed()));
    }
}
