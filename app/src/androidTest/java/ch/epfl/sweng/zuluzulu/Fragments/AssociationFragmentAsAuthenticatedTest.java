package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.User;
import ch.epfl.sweng.zuluzulu.TestingUtility.Utility;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AssociationFragmentAsAuthenticatedTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    @Before
    public void initAuthenticatedTest() {
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
    public void authenticatedClickOnFavorites() {
        onView(ViewMatchers.withId(R.id.association_fragment_fav_button)).perform(ViewActions.click());
        //onView(withId(R.id.association_fragment_fav_button)).check(matches(hasBackground(R.color.colorTransparent)));
    }

    @Test
    public void authenticatedStayOnAll() {
        onView(withId(R.id.association_fragment_all_button)).perform(ViewActions.click());
        //onView(withId(R.id.association_fragment_listview)).check(matches(hasChildCount(Utility.NUMBER_OF_ASSOCIATIONS)));
    }

    @Test
    public void clickOnAssociation1() {
        onView(withId(R.id.association_fragment_fav_button)).perform(ViewActions.click());
        onView(withText("Agepoly")).perform(ViewActions.click());
    }

    @Test
    public void clickOnAssociation2() {
        onView(withId(R.id.association_fragment_all_button)).perform(ViewActions.click());
        onView(withText("Club Montagne")).perform(ViewActions.click());
    }


}
