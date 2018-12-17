package ch.epfl.sweng.zuluzulu.fragments;

import android.support.test.espresso.action.ViewActions;

import org.junit.Test;

import ch.epfl.sweng.zuluzulu.database.MockedProxy;
import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.testingUtility.TestWithAuthenticatedAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MainFragmentAuthentUserTest extends TestWithAuthenticatedAndFragment<MainFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());
        fragment = MainFragment.newInstance(user);
    }

    @Test
    public void elementsArePresent() {
        onView(withId(R.id.main_fragment_followed_associations_text)).check(matches(isDisplayed()));
        onView(withId(R.id.main_fragment_followed_events_text)).check(matches(isDisplayed()));
        onView(withId(R.id.main_fragment_followed_associations_listview)).check(matches(isDisplayed()));
        onView(withId(R.id.main_fragment_followed_events_listview)).check(matches(isDisplayed()));
    }

    @Test
    public void anElementIspresent() {
        onView(withId(R.id.main_fragment_followed_associations_listview)).check(matches(hasDescendant(withText("Agepoly"))));
        onView(withId(R.id.main_fragment_followed_events_listview)).check(matches(hasDescendant(withText("Fiesta time"))));
    }


    @Test
    public void testUserCanSwipeUp() {
        onView(withId(R.id.swiperefresh_main_user)).perform(ViewActions.swipeDown());
    }
}
