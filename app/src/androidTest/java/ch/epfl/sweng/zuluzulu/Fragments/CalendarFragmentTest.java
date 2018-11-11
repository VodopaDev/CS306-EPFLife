package ch.epfl.sweng.zuluzulu.Fragments;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestWithLogin;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class CalendarFragmentTest  extends TestWithLogin {
    private CalendarFragment fragment;

    @Before
    public void init() {
        // Register the idling resource
        IdlingRegistry.getInstance().register(mActivityRule.getActivity().getCountingIdlingResource());
        fragment = CalendarFragment.newInstance(getUser());
        mActivityRule.getActivity().openFragment(fragment);
    }

    @Test
    public void fragmentHasAllElements(){
        onView(withId(R.id.calendar_view)).check(matches(isDisplayed()));
        onView(withId(R.id.calendar_list)).check(matches(isDisplayed()));
    }

    @Test
    public void clickingOn15NovemberDisplaysForumEPFL(){
        onView(withText("15")).perform(ViewActions.click());
        onView(withText("ForumEPFL")).check(matches(isDisplayed()));
    }

}
