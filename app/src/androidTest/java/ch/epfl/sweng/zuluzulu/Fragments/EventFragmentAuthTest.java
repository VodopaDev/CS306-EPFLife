package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedAndFragment;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class EventFragmentAuthTest extends TestWithAuthenticatedAndFragment<EventFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());
        fragment = EventFragment.newInstance(user);
    }

    @Override
    @Before
    public void init() {
        super.init();
        onView(withId(R.id.event_fragment_filter_button)).perform(click());
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
        onView(withId(R.id.event_fragment_listview)).check(matches(hasMinimumChildCount(1)));
        onView(withId(R.id.event_fragment_listview)).check(matches(hasDescendant(withText("Fiesta time"))
        ));
    }

    @Test
    public void sortEventWithTheThreeSimpleSort() {
        onView(withId(R.id.event_fragment_checkBox_sort_date)).perform(ViewActions.click());
        onView(withId(R.id.event_fragment_checkbox_sort_like)).perform(ViewActions.click());
        onView(withId(R.id.event_fragment_checkBox_sort_name)).perform(ViewActions.click());
    }

    @Test
    public void sortWithKeywordNoResultTest() {
        onView(withId(R.id.event_fragment_search_bar)).perform(typeText("random test that produces no result"));
        onView(withId(R.id.event_fragment_listview)).check(matches(not(hasDescendant(withText("Fiesta time")))));
        onView(withId(R.id.event_fragment_listview)).check(matches(hasChildCount(0)));
    }

    @Test
    public void sortWithKeywordNameTest() {
        onView(withId(R.id.event_fragment_search_bar)).perform(typeText("Fiesta time"));
        onView(withId(R.id.event_fragment_listview)).check(matches(hasDescendant(withText("Fiesta time"))));
    }

    @Test
    public void sortWithKeywordShortDescTest() {
        onView(withId(R.id.event_fragment_search_bar)).perform(typeText("Is this a real event"));
        onView(withId(R.id.event_fragment_listview)).check(matches(hasDescendant(withText("Fiesta time"))));
    }

    @Test
    public void sortWithKeywordDescTest() {
        onView(withId(R.id.event_fragment_search_bar)).perform(typeText("Of course not, you should check this beautiful description"));
        onView(withId(R.id.event_fragment_listview)).check(matches(hasDescendant(withText("Fiesta time"))));
    }

    @Test
    public void sortWithDate() {
        onView(withId(R.id.event_fragment_from_date)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.event_fragment_to_date)).perform(click());
        onView(withText("OK")).perform(click());
    }

    @Test
    public void datepickerOutOfBandDateSanitazition() {
        onView(withId(R.id.event_fragment_from_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(1990, 1, 1));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.event_fragment_from_date)).check(matches(withText("01/01/00")));
        onView(withId(R.id.event_fragment_to_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2030, 1, 1));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.event_fragment_to_date)).check(matches(withText("01/01/25")));
    }

    @Test
    public void toDateLowerThanFromDate() {
        onView(withId(R.id.event_fragment_from_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2018, 1, 1));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.event_fragment_to_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2017, 1, 1));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.event_fragment_from_date)).check(matches(withText("01/01/17")));
        onView(withId(R.id.event_fragment_to_date)).check(matches(withText("01/01/18")));
    }

    @Test
    public void filterWithDateAddAnEventInEventFilteredList() {
        onView(withId(R.id.event_fragment_from_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2017, 1, 1));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.event_fragment_to_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2019, 1, 1));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.event_fragment_listview)).check(matches(hasMinimumChildCount(1)));
    }

    @Test
    public void filterWithDateDontAddAnEventInEventFilteredList() {
        onView(withId(R.id.event_fragment_from_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2001, 1, 1));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.event_fragment_to_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2002, 1, 1));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.event_fragment_listview)).check(matches(hasChildCount(0)));
    }

    @Test
    public void authenticatedCanClickFavorite() {
        onView(withId(R.id.event_fragment_listview)).check(matches(hasChildCount(1)));
        onView(withId(R.id.event_fragment_fav_button)).perform(click());
        onView(withId(R.id.event_fragment_listview)).check(matches(hasChildCount(1)));
    }

    @Test
    public void authenticatedCanUnfollowAndFollowAnEvent() {
        onView(withId(R.id.event_fragment_listview)).check(matches(hasChildCount(1)));
        onData(anything())
                .inAdapterView(withId(R.id.event_fragment_listview))
                .atPosition(0)
                .onChildView(withId(R.id.card_event_like_button))
                .perform(click());
        onView(withId(R.id.event_fragment_fav_button)).perform(click());
        onView(withId(R.id.event_fragment_listview)).check(matches(hasChildCount(0)));
        onView(withId(R.id.event_fragment_all_button)).perform(click());
        onView(withId(R.id.event_fragment_listview)).check(matches(hasChildCount(1)));
        onData(anything())
                .inAdapterView(withId(R.id.event_fragment_listview))
                .atPosition(0)
                .onChildView(withId(R.id.card_event_like_button))
                .perform(click());
        onView(withId(R.id.event_fragment_fav_button)).perform(click());
        onView(withId(R.id.event_fragment_listview)).check(matches(hasChildCount(1)));
    }

    @Test
    public void optionPanelIsDisplayed() {
        onView(withId(R.id.even_filter_constraintLayout)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnEvent() {
        onView(withText("Fiesta time")).perform(click());
    }

    @Test
    public void optionPanelIsNotDisplayed() {
        onView(withId(R.id.event_fragment_filter_button)).perform(click());
        onView(withId(R.id.even_filter_constraintLayout)).check(matches(not(isDisplayed())));
    }
}