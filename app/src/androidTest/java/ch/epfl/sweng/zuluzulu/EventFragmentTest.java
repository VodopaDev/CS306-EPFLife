package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.Fragments.EventFragment;
import ch.epfl.sweng.zuluzulu.Fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.Structure.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class EventFragmentTest {

    private static final int NB_ALL_EVENTS = 4;
    private static final int NB_FAV_EVENTS = 3;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    private User user;
    private SuperFragment fragment;

    @Before
    public void initAuthenticatedTest() {
        user = Utility.createTestUser();
        Utility.addUserToMainIntent(mActivityRule, user);
        fragment = EventFragment.newInstance(user);
        mActivityRule.getActivity().openFragment(fragment);

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
    public void LikeAnEvent() {
        onView(withId(R.id.card_event_like_button)).perform(ViewActions.click());
    }
}