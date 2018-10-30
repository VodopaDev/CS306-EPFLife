package ch.epfl.sweng.zuluzulu;

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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class EventFragmentTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    private User user;
    private SuperFragment fragment;

    @Before
    public void init() {
        user = Utility.createTestUser();
        Utility.addUserToMainIntent(mActivityRule, user);
        fragment = EventFragment.newInstance(user);
        mActivityRule.getActivity().openFragment(fragment);
    }

    @Test
    public void thereAreTwoButtons() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        onView(withId(R.id.event_fragment_fav_button)).check(matches(isDisplayed()));
        onView(withId(R.id.event_fragment_all_button)).check(matches(isDisplayed()));
        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    public void clickOnFavThenOnAll() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        onView(withId(R.id.event_fragment_fav_button)).perform(ViewActions.click());
        TimeUnit.SECONDS.sleep(2);
        onView(withId(R.id.event_fragment_all_button)).perform(ViewActions.click());
        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    public void thereAreTwoSortCheckbox() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        onView(withId(R.id.event_fragment_checkBox_sort_name)).check(matches(isDisplayed()));
        onView(withId(R.id.event_fragment_checkBox_sort_date)).check(matches(isDisplayed()));
        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    public void listAlternateSortOption() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        onView(withId(R.id.event_fragment_checkBox_sort_date)).perform(ViewActions.click());
        TimeUnit.SECONDS.sleep(2);
        onView(withId(R.id.event_fragment_checkBox_sort_name)).perform(ViewActions.click());
        TimeUnit.SECONDS.sleep(2);
    }
}