package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Fragments.EventFragment;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class EventFragmentTest extends TestWithAuthenticatedAndFragment<EventFragment> {

    @Override
    public void initFragment() {
        fragment = EventFragment.newInstance(user);
    }

    @Test
    public void thereAreTwoButtons() {
        onView(ViewMatchers.withId(R.id.event_fragment_fav_button)).check(matches(isDisplayed()));
        onView(withId(R.id.event_fragment_all_button)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnFavThenOnAll() {
        onView(withId(R.id.event_fragment_fav_button)).perform(ViewActions.click());
        onView(withId(R.id.event_fragment_all_button)).perform(ViewActions.click());
    }

    @Test
    public void thereAreTwoSortCheckbox() {
        onView(withId(R.id.event_fragment_checkBox_sort_name)).check(matches(isDisplayed()));
        onView(withId(R.id.event_fragment_checkBox_sort_date)).check(matches(isDisplayed()));
    }
//
//    @Test
//    public void listAlternateSortOption() throws InterruptedException {
//        onView(withId(R.id.event_fragment_checkBox_sort_date)).perform(ViewActions.click());
//        onView(withId(R.id.event_fragment_checkBox_sort_name)).perform(ViewActions.click());
//    }
    @Test
    public void sortListDateFrom() {
        onView(withId(R.id.event_fragment_from_date)).perform(typeText("01012040")).perform(closeSoftKeyboard());
        onView(withId(R.id.event_fragment_checkBox_sort_date)).perform(ViewActions.click());
    }

    @Test
    public void sortListDateFromAndTo() {
        onView(withId(R.id.event_fragment_from_date)).perform(typeText("01012040")).perform(closeSoftKeyboard());
        onView(withId(R.id.event_fragment_to_date)).perform(typeText("01012041")).perform(closeSoftKeyboard());
        onView(withId(R.id.event_fragment_checkBox_sort_date)).perform(ViewActions.click());
    }

    //
    // Not used test
    //
//    @Test
//    public void guestMainPageHasSomeEvent() throws InterruptedException {
//        onView(withId(R.id.event_fragment_all_button)).perform(ViewActions.click());
//        assertThat(list_events, hasChildCount(NB_ALL_EVENTS));
//        */
//    }

//    @Test
//    public void guestClickOnFavoritesStaysOnAll() throws InterruptedException {
//        onView(withId(R.id.event_fragment_fav_button)).perform(ViewActions.click());
//        assertThat(list_events, hasChildCount(NB_ALL_EVENTS));
//    }

//    @Test
//    public void authenticatedClickOnFavoritesDisplayFewerEvents() throws InterruptedException {
//        onView(withId(R.id.event_fragment_fav_button)).perform(ViewActions.click());
//        assertThat(list_events, hasChildCount(NB_FAV_EVENTS));
//    }

//    @Test
//    public void listSortByName() throws InterruptedException {
//        onView(withId(R.id.event_fragment_checkBox_sort_name)).perform(ViewActions.click());
//    }

//    @Test
//    public void listSortByDate() throws InterruptedException {
//        onView(withId(R.id.event_fragment_checkBox_sort_date)).perform(ViewActions.click());
//    }


//    @Test
//    public void AuthenticatedClickingAnEventGoesToDetail() throws InterruptedException {
//        authenticatedGoesToEvent();
//        onView(withText("forumEpfl")).perform(ViewActions.click());
//        onView(withId(R.id.event_detail_name)).check(matches(isDisplayed()));
//    }
}