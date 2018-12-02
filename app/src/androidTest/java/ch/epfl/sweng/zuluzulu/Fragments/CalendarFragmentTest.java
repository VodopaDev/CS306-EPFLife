package ch.epfl.sweng.zuluzulu.Fragments;
import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestingUtility.MockedProxy;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class CalendarFragmentTest  extends TestWithAuthenticatedAndFragment<CalendarFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());

        fragment = CalendarFragment.newInstance(user);
    }

    @Test
    public void fragmentHasAllElements(){
        onView(withId(R.id.calendar_view)).check(matches(isDisplayed()));
        onView(withId(R.id.calendar_list)).check(matches(isDisplayed()));
    }

    @Test
    public void clickingOn15NovemberDisplaysOneEvent(){
        onView(withText("15")).perform(ViewActions.click());
    }

    @Test
    public void clickingOn14NovemberDisplaysNothing(){
        onView(withText("14")).perform(ViewActions.click());
    }

}
