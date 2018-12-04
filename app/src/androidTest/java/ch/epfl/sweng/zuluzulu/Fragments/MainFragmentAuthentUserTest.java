package ch.epfl.sweng.zuluzulu.Fragments;

import org.junit.Before;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.TestWithAuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MainFragmentAuthentUserTest extends TestWithAuthenticatedUser {
    SuperFragment fragment;

    @Before
    public void init() {
        DatabaseFactory.setDependency(new MockedProxy());
        fragment = MainFragment.newInstance(getUser());
        mActivityRule.getActivity().openFragment(fragment);
    }

    @Test
    public void elementsArePresent(){
        onView(withId(R.id.main_fragment_image)).check(matches(isDisplayed()));
        onView(withId(R.id.main_fragment_followed_associations_text)).check(matches(isDisplayed()));
        onView(withId(R.id.main_fragment_followed_events_text)).check(matches(isDisplayed()));
        onView(withId(R.id.main_fragment_followed_associations_listview)).check(matches(isDisplayed()));
        onView(withId(R.id.main_fragment_followed_events_listview)).check(matches(isDisplayed()));
    }

    @Test
    public void anElementIspresent(){
        onView(withId(R.id.main_fragment_followed_associations_listview)).check(matches(hasDescendant(withText("Agepoly"))));
        onView(withId(R.id.main_fragment_followed_events_listview)).check(matches(hasDescendant(withText("Fiesta time"))));
    }
}
