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
import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AssociationDetailFragmentTest {
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
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void thereIsSomething(){
        onView(withText("Agepoly")).perform(ViewActions.click());
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void thereIsNothing(){
        onView(withText("ForumEPFL")).perform(ViewActions.click());
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    @Test
    public void authenticatedCanRemoveAndAddFavorite() {
        onView(withId(R.id.association_detail_fav)).perform(ViewActions.click());
        assertThat(false, equalTo(user.isFavAssociation(frag.getAsso())));
        onView(withId(R.id.association_detail_fav)).perform(ViewActions.click());
        assertThat(true, equalTo(user.isFavAssociation(frag.getAsso())));
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
