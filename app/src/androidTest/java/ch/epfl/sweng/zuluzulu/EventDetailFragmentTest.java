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
public class EventDetailFragmentTest {

    private static final String FAV_CONTENT = "This event is in your favorites";
    private static final String NOT_FAV_CONTENT = "This event isn't in your favorites";

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    private void guestGoesToEventDetail() {
        Utility.goToEvent();
//        TimeUnit.SECONDS.sleep(1);
    }

    private void authenticatedGoesToEvent() throws InterruptedException {
        Utility.fullLogin();
        TimeUnit.SECONDS.sleep(3);
        Utility.goToEvent();
//        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void authenticatedAlreadyHasForumEpflInFavorite() throws InterruptedException {
        authenticatedGoesToEvent();
        TimeUnit.SECONDS.sleep(3);
        onView(withText("forumEpfl")).perform(ViewActions.click());
        TimeUnit.SECONDS.sleep(3);
        onView(withContentDescription(FAV_CONTENT)).check(matches(isDisplayed()));
    }

    @Test
    public void authenticatedCanRemoveAndAddFavorite() throws InterruptedException {
        authenticatedGoesToEvent();
        TimeUnit.SECONDS.sleep(3);
        onView(withText("forumEpfl")).perform(ViewActions.click());
        TimeUnit.SECONDS.sleep(3);
        onView(withContentDescription(FAV_CONTENT))
                .check(matches(isDisplayed()))
                .perform(ViewActions.click());
        TimeUnit.SECONDS.sleep(3);
        onView(withContentDescription(NOT_FAV_CONTENT))
                .check(matches(isDisplayed()))
                .perform(ViewActions.click());
        TimeUnit.SECONDS.sleep(3);
        onView(withContentDescription(FAV_CONTENT))
                .check(matches(isDisplayed()));
    }

    @Test
    public void guestCanClickOnFavorite() throws InterruptedException {
        guestGoesToEventDetail();
        TimeUnit.SECONDS.sleep(3);
        onView(withText("forumEpfl")).perform(ViewActions.click());
        TimeUnit.SECONDS.sleep(3);
        onView(withContentDescription(NOT_FAV_CONTENT))
                .check(matches(isDisplayed()))
                .perform(ViewActions.click());
        TimeUnit.SECONDS.sleep(3);
        onView(withContentDescription(NOT_FAV_CONTENT))
                .check(matches(isDisplayed()));
    }

}
