package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AssociationDetailFragmentTest {

    private static final String FAV_CONTENT = "This association is in your favorites";
    private static final String NOT_FAV_CONTENT = "This association isn't in your favorites";

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    private void guestGoesToAssociationDetail() throws InterruptedException {
        Utility.goToAssociation();
        TimeUnit.SECONDS.sleep(5);
    }

    private void authenticatedGoesToAssociation() throws InterruptedException {
        Utility.fullLogin();
        Utility.goToAssociation();
        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void authenticatedAlreadyHasAgepolyInFavorite() throws InterruptedException {
        authenticatedGoesToAssociation();
        onView(withText("Agepoly")).perform(ViewActions.click());
        onView(withContentDescription(FAV_CONTENT)).check(matches(isDisplayed()));
    }

    @Test
    public void authenticatedCanRemoveAndAddFavorite() throws InterruptedException {
        authenticatedGoesToAssociation();
        onView(withText("Agepoly")).perform(ViewActions.click());
        onView(withContentDescription(FAV_CONTENT))
                .check(matches(isDisplayed()))
                .perform(ViewActions.click());
        onView(withContentDescription(NOT_FAV_CONTENT))
                .check(matches(isDisplayed()))
                .perform(ViewActions.click());
        onView(withContentDescription(FAV_CONTENT))
                .check(matches(isDisplayed()));
    }

    @Test
    public void guestCanClickOnFavorite() throws InterruptedException {
        guestGoesToAssociationDetail();
        onView(withText("Agepoly")).perform(ViewActions.click());
        onView(withContentDescription(NOT_FAV_CONTENT))
                .check(matches(isDisplayed()))
                .perform(ViewActions.click());
        onView(withContentDescription(NOT_FAV_CONTENT))
                .check(matches(isDisplayed()));
    }

}
