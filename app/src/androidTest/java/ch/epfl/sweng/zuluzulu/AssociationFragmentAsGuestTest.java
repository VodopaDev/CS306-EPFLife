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

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.startsWith;

@RunWith(AndroidJUnit4.class)
public class AssociationFragmentAsGuestTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    private void waitFor(int millis) {
        try{
            Thread.sleep(millis);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    @Before
    public void goToAssociationList() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_associations));
    }

    @Test
    public void thereAreTwoButtons(){
        onView(withId(R.id.association_fragment_all_button)).check(matches(isDisplayed()));
        onView(withId(R.id.association_fragment_all_button)).check(matches(isDisplayed()));
    }

    @Test
    public void mainPageHasSomeAssociations(){
        waitFor(5000);
        onView(withId(R.id.association_fragment_all_button)).perform(ViewActions.click());
        onView(withText("Agepoly")).check(matches(isDisplayed()));
        onView(withText("Clic")).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnFavoritesDisplayToast() {
        onView(withId(R.id.association_fragment_fav_button)).perform(ViewActions.click());
        onView(withText(startsWith("Login to access"))).check(matches(isDisplayed()));
    }
}