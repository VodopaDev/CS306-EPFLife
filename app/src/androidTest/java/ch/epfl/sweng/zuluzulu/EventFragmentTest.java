package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ListView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.Fragments.ChannelFragment;
import ch.epfl.sweng.zuluzulu.Fragments.EventFragment;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.Structure.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class EventFragmentTest {

    private static final int NB_ALL_EVENTS = 4;
    private static final int NB_FAV_EVENTS = 3;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    private User user;
    private Fragment fragment;
//    private int fragment_listview;

    @Before
    public void init() {
        user = Utility.createTestUser();
        Utility.addUserToMainIntent(mActivityRule, user);

        fragment = EventFragment.newInstance(user);
        mActivityRule.getActivity().openFragment(fragment);
    }

    @Test
    public void thereAreTwoButtons() throws InterruptedException {
        onView(withId(R.id.event_fragment_all_button)).check(matches(isDisplayed()));
        onView(withId(R.id.event_fragment_fav_button)).check(matches(isDisplayed()));
    }

    @Test
    public void guestMainPageHasSomeEvent() throws InterruptedException {
        onView(withId(R.id.event_fragment_all_button)).perform(ViewActions.click());
        /*
        TimeUnit.SECONDS.sleep(1);
        assertThat(list_events, hasChildCount(NB_ALL_EVENTS));
        */
    }

    @Test
    public void guestClickOnFavoritesStaysOnAll() throws InterruptedException {
//        guestGoesToEvent();
        onView(withId(R.id.event_fragment_fav_button)).perform(ViewActions.click());
        /*
        TimeUnit.SECONDS.sleep(1);
        assertThat(list_events, hasChildCount(NB_ALL_EVENTS));
        */
    }

    @Test
    public void authenticatedClickOnFavoritesDisplayFewerEvents() throws InterruptedException {
//        authenticatedGoesToEvent();
        onView(withId(R.id.event_fragment_fav_button)).perform(ViewActions.click());
        /*
        TimeUnit.SECONDS.sleep(1);
        assertThat(list_events, hasChildCount(NB_FAV_EVENTS));
        */
    }

    @Test
    public void listSortByName() {
        onView(withId(R.id.event_fragment_checkBox_sort_name)).perform(ViewActions.click());
    }

    @Test
    public void listSortByDate() {
        onView(withId(R.id.event_fragment_checkBox_sort_date)).perform(ViewActions.click());
    }

//    @Test
//    public void AuthenticatedClickingAnEventGoesToDetail() throws InterruptedException {
//        authenticatedGoesToEvent();
//        TimeUnit.SECONDS.sleep(1);
//        onView(withText("forumEpfl")).perform(ViewActions.click());
//        onView(withId(R.id.event_detail_name)).check(matches(isDisplayed()));
//    }
}