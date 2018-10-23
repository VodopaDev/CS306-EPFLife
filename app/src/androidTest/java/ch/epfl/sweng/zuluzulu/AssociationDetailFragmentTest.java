package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.action.CloseKeyboardAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.Fragments.AssociationDetailFragment;
import ch.epfl.sweng.zuluzulu.Fragments.AssociationFragment;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.User;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(AndroidJUnit4.class)
public class AssociationDetailFragmentTest {

    private static final String FAV_CONTENT = "This association is in your favorites";
    private static final String NOT_FAV_CONTENT = "This association isn't in your favorites";
    private AssociationDetailFragment frag;
    private AuthenticatedUser user;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    @Before
    public void initAuthenticatedTest(){
        user = (AuthenticatedUser)Utility.createTestUser();
        Utility.addUserToMainIntent(mActivityRule, user);
        AssociationFragment fragment = AssociationFragment.newInstance(user);
        mActivityRule.getActivity().openFragment(fragment);

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withText("Agepoly")).perform(ViewActions.click());
        assert(mActivityRule.getActivity().getCurrentFragment() instanceof AssociationDetailFragment);
        frag = (AssociationDetailFragment)mActivityRule.getActivity().getCurrentFragment();
    }

    @Test
    public void authenticatedCanRemoveAndAddFavorite() {
        onView(withId(R.id.association_detail_fav)).perform(ViewActions.click());
        assertThat(false, equalTo(user.isFavAssociation(frag.getAsso())));

        onView(withContentDescription(NOT_FAV_CONTENT))
                .check(matches(isDisplayed()))
                .perform(ViewActions.click());

        assertThat(true, equalTo(user.isFavAssociation(frag.getAsso())));
    }

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
