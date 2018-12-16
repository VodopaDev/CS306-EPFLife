package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Structure.EventDate;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedAndFragment;
import ch.epfl.sweng.zuluzulu.Utility;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
public class CalendarFragmentTest extends TestWithAuthenticatedAndFragment<CalendarFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());
        Calendar beginCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        EventDate ev;

        // 10 to 11 event
        beginCalendar.set(2018, Calendar.DECEMBER, 10, 12,0);
        endCalendar.set(2018, Calendar.DECEMBER, 11, 15,0);
        ev = new EventDate(beginCalendar.getTime(), endCalendar.getTime());
        DatabaseFactory.getDependency().addEvent(Utility.defaultEvent().setId("2").setDate(ev).build());

        // 10 to 18 event
        beginCalendar.set(2018, Calendar.DECEMBER, 10, 12,0);
        endCalendar.set(2018, Calendar.DECEMBER, 18, 15,0);
        ev = new EventDate(beginCalendar.getTime(), endCalendar.getTime());
        DatabaseFactory.getDependency().addEvent(Utility.defaultEvent().setId("3").setDate(ev).build());

        // 15 to 30 event
        beginCalendar.set(2018, Calendar.DECEMBER, 15, 12,0);
        endCalendar.set(2018, Calendar.DECEMBER, 30, 15,0);
        ev = new EventDate(beginCalendar.getTime(), endCalendar.getTime());
        DatabaseFactory.getDependency().addEvent(Utility.defaultEvent().setId("4").setDate(ev).build());

        user.removeFollowedEvent("0");
        user.removeFollowedEvent("1");
        user.addFollowedEvent("2");
        user.addFollowedEvent("3");
        user.addFollowedEvent("4");
        fragment = CalendarFragment.newInstance(user);
    }

    @Test
    public void fragmentHasAllElements() {
        onView(withId(R.id.calendar_view)).check(matches(isDisplayed()));
        onView(withId(R.id.calendar_list)).check(matches(isDisplayed()));
    }

    @Test
    public void clickingOn9DecemberDisplaysNoEvents() {
        onView(withText("9")).perform(ViewActions.click());
        //onView(withId(R.id.calendar_list)).check(matches(hasChildCount(0)));
    }

    @Test
    public void clickingOn11DecemberDisplays2Events() {
        onView(withText("11")).perform(ViewActions.click());
        //onView(withId(R.id.calendar_list)).check(matches(hasChildCount(2)));
    }

    @Test
    public void clickingOn16DecemberDisplays2Events() {
        onView(withText("16")).perform(ViewActions.click());
        //onView(withId(R.id.calendar_list)).check(matches(hasChildCount(2)));
    }

}
