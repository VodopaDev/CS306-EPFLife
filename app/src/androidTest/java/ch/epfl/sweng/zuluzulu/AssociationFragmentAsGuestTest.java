package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.bumptech.glide.util.Util;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.Fragments.AssociationFragment;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.Guest;
import ch.epfl.sweng.zuluzulu.Structure.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AssociationFragmentAsGuestTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    @Before
    public void initGuestTest(){
        Guest guest = new User.UserBuilder().buildGuestUser();
        Utility.addUserToMainIntent(mActivityRule, guest);
        AssociationFragment fragment = AssociationFragment.newInstance(guest);
        mActivityRule.getActivity().openFragment(fragment);

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void thereAreTwoButtons() {
        onView(withId(R.id.association_fragment_all_button)).check(matches(isDisplayed()));
        onView(withId(R.id.association_fragment_all_button)).check(matches(isDisplayed()));
    }

    @Test
    public void guestMainPageHasSomeAssociations() {
        onView(withId(R.id.association_fragment_all_button)).perform(ViewActions.click());
    }

    @Test
    public void guestClickOnFavoritesStaysOnAll() {
        onView(withId(R.id.association_fragment_fav_button)).perform(ViewActions.click());
    }


    @Test
    public void clickingAnAssociationGoesToDetail() {
        onView(withText("Agepoly")).perform(ViewActions.click());
        onView(withId(R.id.association_detail_name)).check(matches(isDisplayed()));
    }
}