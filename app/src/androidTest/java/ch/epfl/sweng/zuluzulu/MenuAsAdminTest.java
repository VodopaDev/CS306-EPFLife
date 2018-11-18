package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.contrib.NavigationViewActions;

import org.junit.Before;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.Fragments.MainFragment;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAdminAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class MenuAsAdminTest extends TestWithAdminAndFragment<MainFragment> {

    @Override
    public void initFragment() {
        fragment = MainFragment.newInstance(user);
    }

    @Test
    public void testCanOpenAssociationsGenerator() {
        Utility.openMenu();
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_associations_generator));
        Utility.checkFragmentIsOpen(R.id.associations_generator_fragment);
    }

    @Test
    public void testCanOpenMemento() {
        Utility.openMenu();
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_memento_admin));
        Utility.checkFragmentIsOpen(R.id.memento_fragment);
    }
}
