package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.Fragments.AssociationFragment;
import ch.epfl.sweng.zuluzulu.Structure.Guest;
import ch.epfl.sweng.zuluzulu.Structure.User;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AssociationDetailFragmentTest {

    private static final String FAV_CONTENT = "This association is in your favorites";
    private static final String NOT_FAV_CONTENT = "This association isn't in your favorites";

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    @Before
    public void initAuthenticatedTest(){
        User user = Utility.createTestUser();
        Utility.addUserToMainIntent(mActivityRule, user);
        AssociationFragment fragment = AssociationFragment.newInstance(user);
        mActivityRule.getActivity().openFragment(fragment);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void authenticatedAlreadyHasAgepolyInFavorite() {
        onView(withText("Agepoly")).perform(ViewActions.click());
        onView(withContentDescription(FAV_CONTENT)).check(matches(isDisplayed()));
    }

    /*
    @Test
    public void authenticatedCanRemoveAndAddFavorite() {
        onView(withText("Agepoly")).perform(ViewActions.click());
        onView(withContentDescription(FAV_CONTENT))
                .check(matches(isDisplayed()));
        onView(withId(R.id.association_detail_fav))
                .check(matches(isClickable()))
                .perform(ViewActions.click());
        onView(withContentDescription(NOT_FAV_CONTENT))
                .check(matches(isDisplayed()));
        onView(withId(R.id.association_detail_fav)).perform(ViewActions.click());
        onView(withContentDescription(FAV_CONTENT))
                .check(matches(isDisplayed()));
    }
    */

    /*
    @Test
    public void guestCantClickOnFavorite() {
        initGuestTest();
        onView(withText("Agepoly")).perform(ViewActions.click());
        onView(withContentDescription(NOT_FAV_CONTENT))
                .check(matches(isDisplayed()))
                .perform(ViewActions.click());
        onView(withContentDescription(NOT_FAV_CONTENT))
                .check(matches(isDisplayed()));
    }
    */

}
