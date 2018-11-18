package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ListView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.Adapters.EventArrayAdapter;
import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestWithAuthenticatedUser;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

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
        onView(withId(R.id.event_fragment_fav_button)).check(matches(isDisplayed()));
        onView(withId(R.id.event_fragment_all_button)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnFavThenOnAll() throws InterruptedException {
        onView(withId(R.id.event_fragment_fav_button)).perform(ViewActions.click());
        onView(withId(R.id.event_fragment_all_button)).perform(ViewActions.click());
    }

    @Test
    public void thereAreThreeSortCheckbox() throws InterruptedException {
        onView(withId(R.id.event_fragment_checkBox_sort_name)).check(matches(isDisplayed()));
        onView(withId(R.id.event_fragment_checkBox_sort_date)).check(matches(isDisplayed()));
        onView(withId(R.id.event_fragment_checkbox_sort_like)).check(matches(isDisplayed()));
    }

    @Test
    public void thereAreTwoEditText() throws InterruptedException {
        onView(withId(R.id.event_fragment_from_date)).check(matches(isDisplayed()));
        onView(withId(R.id.event_fragment_to_date)).check(matches(isDisplayed()));
    }

    @Test
    public void sortEventWithTheThreeSimpleSort() throws InterruptedException{
        onView(withId(R.id.event_fragment_checkBox_sort_date)).perform(ViewActions.click());
        onView(withId(R.id.event_fragment_checkbox_sort_like)).perform(ViewActions.click());
        onView(withId(R.id.event_fragment_checkBox_sort_name)).perform(ViewActions.click());
    }

    @Test
    public void sortWithKeyworkTest() {
        onView(withId(R.id.event_fragment_search_bar)).perform(typeText("forum"));

        onData(instanceOf(EventFragment.class))
                .inAdapterView(withId(R.id.event_fragment_listview))
                .atPosition(1)
//                .onChildView(withId(R.id.card_event_name))
                .perform(click());

//        onView(withId(R.id.event_fragment_checkBox_sort_name)).perform(ViewActions.click());
    }

    @Test
    public void sortFromDate() throws InterruptedException {
        onView(withId(R.id.event_fragment_from_date)).perform(click());
        onView(withText("OK")).perform(click());
    }

    @Test
    public void sortToDate(){

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
}