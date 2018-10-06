package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.Structure.Association;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class AssociationsFragmentTest {
    private Association asso;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void openAssociationsFragment() {
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Click on the login item in the Drawer
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_associations));
    }

    @Test
    public void buttonsTest() {
        onView(withId(R.id.button_assos_fav)).perform(click());
        onView(withId(R.id.scroll_assos_fav)).check(matches(isDisplayed()));
        onView(withId(R.id.button_assos_all)).perform(click());
        onView(withId(R.id.scroll_assos_all)).check(matches(isDisplayed()));
    }

}
