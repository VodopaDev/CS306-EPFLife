package ch.epfl.sweng.zuluzulu.Fragments;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.TestWithAuthenticatedUser;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedAndFragment;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class CalendarFragmentTest  extends TestWithAuthenticatedAndFragment<CalendarFragment> {

    @Override
    public void initFragment() {
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
        try {
            TimeUnit.MILLISECONDS.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.calendar_list))
                .check(matches(hasChildCount(1)));
    }

    @Test
    public void clickingOn14NovemberDisplaysNothing(){
        onView(withText("14")).perform(ViewActions.click());
        onView(withId(R.id.calendar_list))
                .check(matches(hasChildCount(0)));
    }

}
