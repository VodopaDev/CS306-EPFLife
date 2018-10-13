package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
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
import static android.support.test.espresso.matcher.ViewMatchers.hasBackground;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class AssociationFragmentAsAuthenticatedTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    private void openDrawer(){
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open());
    }

    @Before
    public void setUptest(){
        // Authenticate
        openDrawer();
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_login));

        onView(withId(R.id.username)).perform(typeText("nicolas")).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("password")).perform(closeSoftKeyboard());
        onView(withId(R.id.sign_in_button)).perform(click());

        // Go to association view
        openDrawer();
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_associations));
    }

    @Test
    public void buttonsAreClickable(){
        onView(withId(R.id.association_fragment_fav_button))
                .check(matches(isClickable()))
                .perform(ViewActions.click())
                .check(matches(hasBackground(R.color.colorTransparent)));
        onView(withId(R.id.association_fragment_all_button))
                .check(matches(hasBackground(R.color.colorGrayDarkTransparent)));

        onView(withId(R.id.association_fragment_all_button))
                .check(matches(isClickable()))
                .perform(ViewActions.click())
                .check(matches(hasBackground(R.color.colorTransparent)));
        onView(withId(R.id.association_fragment_fav_button))
                .check(matches(hasBackground(R.color.colorGrayDarkTransparent)));

    }
}
