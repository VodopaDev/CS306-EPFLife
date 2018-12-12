package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;

import org.junit.Test;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithGuestAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MainFragmentGuestUserTest extends TestWithGuestAndFragment<MainFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());
        fragment = MainFragment.newInstance(user);
    }

    @Test
    public void clickOnEvent() {
        onView(withText("Fiesta time")).perform(click());
    }

    @Test
    public void clickOnAssos() {
        onView(withText("Agepoly")).perform(click());
    }

    @Test
    public void elementsArePresent() {
        onView(withId(R.id.main_page_upcoming_events)).check(matches(isDisplayed()));
        onView(withId(R.id.main_page_list_event)).check(matches(isDisplayed()));
        onView(withId(R.id.main_page_random_assos)).check(matches(isDisplayed()));
        onView(withId(R.id.main_page_lv_random_assos)).check(matches(isDisplayed()));
    }

    @Test
    public void canClickOnSignIn() {
        onView(withId(R.id.main_page_button_sign_in)).perform(ViewActions.click());
        onView(withId(R.id.sign_in_button)).check(matches(isDisplayed()));

    }
}
