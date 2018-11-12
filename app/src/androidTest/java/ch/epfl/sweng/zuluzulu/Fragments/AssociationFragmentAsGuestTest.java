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

import ch.epfl.sweng.zuluzulu.Fragments.AssociationFragment;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Guest;
import ch.epfl.sweng.zuluzulu.Structure.User;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithGuestAndFragment;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AssociationFragmentAsGuestTest extends TestWithGuestAndFragment<AssociationFragment> {

    @Override
    public void initFragment() {
        fragment = AssociationFragment.newInstance(user);
    }

    @Test
    public void hasAllButtons() {
        onView(ViewMatchers.withId(R.id.association_fragment_all_button)).check(matches(isDisplayed()));
        onView(withId(R.id.association_fragment_all_button)).check(matches(isDisplayed()));
    }

    @Test
    public void guestCantClickOnFavorites(){
        //onView(withText("ForumEPFL")).check(matches(isDisplayed()));
        onView(withId(R.id.association_fragment_fav_button)).perform(ViewActions.click());
        //onView(withText("ForumEPFL")).check(matches(isDisplayed()));
    }

    @Test
    public void guestMainPageHasSomeAssociations() {
        onView(withId(R.id.association_fragment_all_button)).perform(ViewActions.click());
    }

    @Test
    public void guestClickOnFavoritesStaysOnAll() {
        onView(withId(R.id.association_fragment_fav_button)).perform(ViewActions.click());
    }


   /* @Test
    public void clickingAnAssociationGoesToDetail() {
        onView(withText("ForumEPFL")).perform(ViewActions.click());
        onView(withId(R.id.association_detail_icon)).check(matches(isDisplayed()));
    }*/

    @Test
    public void thereAreTwoSortCheckbox() {
        onView(withId(R.id.assos_fragment_checkbox_sort_Name)).check(matches(isDisplayed()));
        onView(withId(R.id.assos_fragment_checkbox_sort_date)).check(matches(isDisplayed()));
    }

    @Test
    public void listAlternateSortOption() {
        onView(withId(R.id.assos_fragment_checkbox_sort_date)).perform(ViewActions.click());
        onView(withId(R.id.assos_fragment_checkbox_sort_Name)).perform(ViewActions.click());
    }
}