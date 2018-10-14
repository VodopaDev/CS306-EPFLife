package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.action.ViewActions;
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
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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
    public void goToAssociationList(){
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

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void mainPageHasSomeAssociations(){
        onView(withId(R.id.association_fragment_all_button)).perform(ViewActions.click());
        onView(withText("Agepoly")).check(matches(isDisplayed()));
        onView(withText("Clic")).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnFavoritesDisplayFewerAssociations() {
        onView(withId(R.id.association_fragment_fav_button)).perform(ViewActions.click());
        onView(withText("Agepoly")).check(matches(isDisplayed()));
        onView(withText("Clic")).check(doesNotExist());
    }


}
