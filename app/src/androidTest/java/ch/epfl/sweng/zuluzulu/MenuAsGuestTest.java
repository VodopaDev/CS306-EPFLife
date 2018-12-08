package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Fragments.MainFragment;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithGuestAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MenuAsGuestTest extends TestWithGuestAndFragment<MainFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());
        fragment = MainFragment.newInstance(user);
    }

    @Before
    @Override
    public void init() {
        super.init();
        Utility.openMenu();
    }

    @Test
    public void testGuestUserDoesNotSeeEveryOption() {
        onView(withText(R.string.drawer_chats)).check(doesNotExist());
        onView(withText(R.string.drawer_logout)).check(doesNotExist());
        onView(withText(R.string.drawer_profile)).check(doesNotExist());
    }

    @Test
    public void testGuestCanOpenMainFragment() {
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_main));
        Utility.checkFragmentIsOpen(R.id.main_fragment);
    }

    @Test
    public void testGuestCanOpenSettingsFragment() {
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_settings));


        Utility.checkFragmentIsOpen(R.id.settings_fragment);
    }

    @Test
    public void testGuestCanOpenLoginFragment() {
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_login));


        Utility.checkFragmentIsOpen(R.id.login_fragment);
    }

    @Test
    public void testCanOpenAboutFragment() {
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_about));

        Utility.checkFragmentIsOpen(R.id.about_fragment);
    }
}