package ch.epfl.sweng.zuluzulu.fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.database.MockedProxy;
import ch.epfl.sweng.zuluzulu.testingUtility.TestWithGuestAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class AssociationFragmentAsGuestTest extends TestWithGuestAndFragment<AssociationFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());

        fragment = AssociationFragment.newInstance(user);
    }

    @Test
    public void hasAllButtons() {
        onView(ViewMatchers.withId(R.id.association_fragment_all_button)).check(matches(isDisplayed()));
        onView(withId(R.id.association_fragment_all_button)).check(matches(isDisplayed()));
        onView(withId(R.id.search_button)).check(matches(isDisplayed()));
        onView(withId(R.id.association_fragment_search_text)).check(matches(isDisplayed()));
    }

    @Test
    public void guestCantClickOnFavorites() {
        onView(withId(R.id.association_fragment_fav_button)).perform(ViewActions.click());
    }

    @Test
    public void guestMainPageHasSomeAssociations() {
        onView(withId(R.id.association_fragment_listview))
                .check(matches(hasChildCount(1)));
    }

    @Test
    public void useTheFilter() {
        onView(withId(R.id.association_fragment_search_text)).perform(ViewActions.typeText("Agepoly"));
    }
}