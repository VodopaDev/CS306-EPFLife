package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.NavigationViewActions;

import org.junit.Test;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Fragments.MainFragment;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAdminAndFragment;

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


    @Test
    public void testCanOpenMemento(){
        testCanOpenAdminPanel();
        onView(withId(R.id.panel_memento)).perform(ViewActions.click());
        Utility.checkFragmentIsOpen(R.id.memento_list_view);
    }

    /*
    @Test
    public void testCanOpenAssociationGenerator(){
        testCanOpenAdminPanel();
        onView(withId(R.id.panel_association)).perform(ViewActions.click());
        Utility.checkFragmentIsOpen(R.id.associations_generator_fragment);
    }
    */

    @Test
    public void testCanOpenUserRole(){
        testCanOpenAdminPanel();
        onView(withId(R.id.panel_user)).perform(ViewActions.click());
        Utility.checkFragmentIsOpen(R.id.user_role_searchbar);
    }

    @Test
    public void testCanOpenCreateEvent(){
        testCanOpenAdminPanel();
        onView(withId(R.id.panel_create_event)).perform(ViewActions.click());
        Utility.checkFragmentIsOpen(R.id.spinner_day);
    }

    //TODO: change when manage channel does something
    @Test
    public void testCanOpenManageChannel(){
        testCanOpenAdminPanel();
        onView(withId(R.id.panel_channel)).perform(ViewActions.click());
        Utility.checkFragmentIsOpen(R.id.panel_channel);
    }
}
