package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestWithAuthenticatedUser;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class EventFragmentTest extends TestWithAuthenticatedUser {
    SuperFragment fragment;

    @Before
    public void init() {
        fragment = EventFragment.newInstance(getUser());
        mActivityRule.getActivity().openFragment(fragment);
    }

    @Test
    public void thereAreTwoButtons() throws InterruptedException {
//        TimeUnit.SECONDS.sleep(1);
        onView(withId(R.id.event_fragment_fav_button)).check(matches(isDisplayed()));
        onView(withId(R.id.event_fragment_all_button)).check(matches(isDisplayed()));
//        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void clickOnFavThenOnAll() throws InterruptedException {
//        TimeUnit.SECONDS.sleep(1);
        onView(withId(R.id.event_fragment_fav_button)).perform(ViewActions.click());
//        TimeUnit.SECONDS.sleep(1);
        onView(withId(R.id.event_fragment_all_button)).perform(ViewActions.click());
//        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void thereAreThreeSortCheckbox() throws InterruptedException {
//        TimeUnit.SECONDS.sleep(1);
        onView(withId(R.id.event_fragment_checkBox_sort_name)).check(matches(isDisplayed()));
        onView(withId(R.id.event_fragment_checkBox_sort_date)).check(matches(isDisplayed()));
        onView(withId(R.id.event_fragment_checkbox_sort_like)).check(matches(isDisplayed()));
//        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void thereAreTwoEditText() throws InterruptedException {
//        TimeUnit.SECONDS.sleep(1);
        onView(withId(R.id.event_fragment_from_date)).check(matches(isDisplayed()));
        onView(withId(R.id.event_fragment_to_date)).check(matches(isDisplayed()));
//        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void sortListDate() throws InterruptedException{
        onView(withId(R.id.event_fragment_checkBox_sort_date)).perform(ViewActions.click());
    }


    @Test
    public void sortListDateFrom() throws InterruptedException {
//        TimeUnit.SECONDS.sleep(1);
        onView(withId(R.id.event_fragment_from_date)).perform(typeText("01012040")).perform(closeSoftKeyboard());
//        TimeUnit.SECONDS.sleep(1);
        onView(withId(R.id.event_fragment_checkBox_sort_date)).perform(ViewActions.click());
//        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void sortListDateFromAndTo() throws InterruptedException {
//        TimeUnit.SECONDS.sleep(1);
        onView(withId(R.id.event_fragment_from_date)).perform(typeText("01012040")).perform(closeSoftKeyboard());
//        TimeUnit.SECONDS.sleep(1);
        onView(withId(R.id.event_fragment_to_date)).perform(typeText("01012041")).perform(closeSoftKeyboard());
//        TimeUnit.SECONDS.sleep(1);
        onView(withId(R.id.event_fragment_checkBox_sort_date)).perform(ViewActions.click());
//        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void sortListByMostLikedEvent() throws InterruptedException{
         onView(withId(R.id.event_fragment_checkbox_sort_like)).perform(ViewActions.click());
    }

    @Test
    public void sortListByName() {
        onView(withId(R.id.event_fragment_checkbox_sort_like)).perform(ViewActions.click());
        onView(withId(R.id.event_fragment_checkBox_sort_name)).perform(ViewActions.click());
    }
}