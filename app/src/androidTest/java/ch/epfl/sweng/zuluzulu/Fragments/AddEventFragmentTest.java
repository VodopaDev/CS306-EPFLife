package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Spinner;

import org.junit.Test;
import org.junit.runner.RunWith;


import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAdminAndFragment;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class AddEventFragmentTest extends TestWithAdminAndFragment<EventFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());
        fragment = EventFragment.newInstance(user);
    }

    /**
     * got to add an event
     */
    private void goToAddEvent(){
        onView(ViewMatchers.withId(R.id.event_add_button)).perform(click());
    }

    /**
     * Test if both fields Title and Description are left empty
     */
    @Test
    public void testEmptyTitleAndDesc() {
        goToAddEvent();
        onView(withId(R.id.create_event_button)).perform(click());

        onView(withId(R.id.add_event_layout)).check(matches(isDisplayed()));
    }

    /**
     * Test if both fields Title and Description have too much text
     */
    @Test
    public void testTooManyCharactersInBoth(){
        goToAddEvent();
        onView(withId(R.id.event_title)).perform(replaceText("This is a title much too long to be able to put it on the database"));
        onView(withId(R.id.long_desc_text)).perform(replaceText("Okay I am now writing a whole story about the life of this test. So once upon a time, a test was created, it was supposed to be super useful and everyone was happy, until they started it, and it failed. And then started hours and hours of debugging" +
                " continuously until it passed. That was the story, thank you")).perform(closeSoftKeyboard());
        onView(withId(R.id.create_event_button)).perform(click());
        onView(withId(R.id.add_event_layout)).check(matches(isDisplayed()));
    }



    /**
     * create an event and controls that it is indeed created in the event list
     */
    @Test
    public void testCreateEvent(){
        goToAddEvent();
        onView(withId(R.id.event_title)).perform(replaceText("Test Event"));
        onView(withId(R.id.long_desc_text)).perform(replaceText("this is an awesome test event")).perform(closeSoftKeyboard());
        onView(withId(R.id.create_event_button)).perform(click());
    }
}
