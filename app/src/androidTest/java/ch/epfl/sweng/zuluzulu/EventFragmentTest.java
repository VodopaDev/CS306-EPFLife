package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class EventFragmentTest {

    private static final int NB_ALL_EVENTS = 7;
    private static final int NB_FAV_EVENTS = 4;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    private void guestGoesToEvent() {
        Utility.goToEvent();
//        TimeUnit.MILLISECONDS.sleep(1);
    }

    private void authenticatedGoesToEvent() {
        Utility.fullLogin();
        Utility.goToEvent();
//        TimeUnit.MILLISECONDS.sleep(1);
    }

    @Test
    public void thereAreTwoButtons() {
        guestGoesToEvent();
        onView(withId(R.id.event_fragment_all_button)).check(matches(isDisplayed()));
        onView(withId(R.id.event_fragment_fav_button)).check(matches(isDisplayed()));
    }

    @Test
    public void guestMainPageHasSomeEvent() {
        guestGoesToEvent();
        onView(withId(R.id.event_fragment_all_button)).perform(ViewActions.click());
        /*
        TimeUnit.SECONDS.sleep(1);
        assertThat(list_events, hasChildCount(NB_ALL_EVENTS));
        */
    }

    @Test
    public void guestClickOnFavoritesStaysOnAll() {
        guestGoesToEvent();
        onView(withId(R.id.event_fragment_fav_button)).perform(ViewActions.click());
        /*
        TimeUnit.SECONDS.sleep(1);
        assertThat(list_events, hasChildCount(NB_ALL_EVENTS));
        */
    }

    @Test
    public void authenticatedClickOnFavoritesDisplayFewerEvents() {
        authenticatedGoesToEvent();
        onView(withId(R.id.event_fragment_fav_button)).perform(ViewActions.click());
        /*
        TimeUnit.SECONDS.sleep(1);
        assertThat(list_events, hasChildCount(NB_FAV_EVENTS));
        */
    }

//    @Test
//    public void AuthenticatedClickingAnEventGoesToDetail() throws InterruptedException {
//        authenticatedGoesToEvent();
//        TimeUnit.SECONDS.sleep(1);
//        onView(withText("forumEpfl")).perform(ViewActions.click());
//        onView(withId(R.id.event_detail_name)).check(matches(isDisplayed()));
//    }
}