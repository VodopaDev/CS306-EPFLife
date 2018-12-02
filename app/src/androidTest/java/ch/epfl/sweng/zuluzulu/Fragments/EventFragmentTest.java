package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestWithAuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Database.MockedProxy;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class EventFragmentTest extends TestWithAuthenticatedUser {
    SuperFragment fragment;

    @Before
    public void init() throws InterruptedException {
        DatabaseFactory.setDependency(new MockedProxy());

        fragment = EventFragment.newInstance(getUser());
        mActivityRule.getActivity().openFragment(fragment);

        onView(withId(R.id.event_fragment_filter_button)).perform(click());

        TimeUnit.SECONDS.sleep(3);
    }

    @Test
    public void thereAreTwoButtons() {
        onView(withId(R.id.event_fragment_fav_button)).check(matches(isDisplayed()));
        onView(withId(R.id.event_fragment_all_button)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnFavThenOnAll() {
        onView(withId(R.id.event_fragment_fav_button)).perform(ViewActions.click());
        onView(withId(R.id.event_fragment_all_button)).perform(ViewActions.click());
    }

    @Test
    public void thereAreThreeSortCheckbox() {
        onView(withId(R.id.event_fragment_checkBox_sort_name)).check(matches(isDisplayed()));
        onView(withId(R.id.event_fragment_checkBox_sort_date)).check(matches(isDisplayed()));
        onView(withId(R.id.event_fragment_checkbox_sort_like)).check(matches(isDisplayed()));
    }

    @Test
    public void thereAreTwoEditText() {
        onView(withId(R.id.event_fragment_from_date)).check(matches(isDisplayed()));
        onView(withId(R.id.event_fragment_to_date)).check(matches(isDisplayed()));
    }

    @Test
    public void thereIsEventInTheListView() {
        onView(withId(R.id.event_fragment_listview)).check(matches(hasMinimumChildCount(4)));
        onView(withId(R.id.event_fragment_listview)).check(matches(hasDescendant(withText("EVENT 1"))
        ));
    }

    @Test
    public void sortEventWithTheThreeSimpleSort()  {
        onView(withId(R.id.event_fragment_checkBox_sort_date)).perform(ViewActions.click());
        onView(withId(R.id.event_fragment_checkbox_sort_like)).perform(ViewActions.click());
        onView(withId(R.id.event_fragment_checkBox_sort_name)).perform(ViewActions.click());
    }

    @Test
    public void sortWithKeywordTest() {
        onView(withId(R.id.event_fragment_search_bar)).perform(typeText("EVENT 1"));
        onView(withId(R.id.event_fragment_listview)).check(matches(hasDescendant(withText("EVENT 1"))));
        onView(withId(R.id.event_fragment_search_bar)).perform(typeText("EVENT 2"));
        onView(withId(R.id.event_fragment_listview)).check(matches(not(hasDescendant(withText("EVENT 1")))));

    //            onData(anything()).inAdapterView(withId(R.id.event_fragment_listview)).onChildView(withId(R.id.card_event_name)).check(matches(withText("EVENT 1")));
    //    onView(withId(R.id.event_fragment_search_bar)).perform(clearText());
    }

    @Test
    public void sortFromDate() {
        onView(withId(R.id.event_fragment_from_date)).perform(click());
        onView(withText("OK")).perform(click());
    }

    @Test
    public void sortToDateFail() {
        onView(withId(R.id.event_fragment_to_date)).perform((click()));
        onView(withId(R.id.event_fragment_to_date)).check(matches(withText("")));
    }

    @Test
    public void sortFromAndToDate() {
        onView(withId(R.id.event_fragment_from_date)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.event_fragment_to_date)).perform((click()));
        onView(withText("OK")).perform(click());
    }

    @Test
    public void optionPanelIsDisplayed() {
        onView(withId(R.id.even_filter_constraintLayout)).check(matches(isDisplayed()));
    }

    @Test
    public void optionPanelIsNotDisplayed() {
        onView(withId(R.id.event_fragment_filter_button)).perform(click());
        onView(withId(R.id.even_filter_constraintLayout)).check(matches(not(isDisplayed())));
    }
}