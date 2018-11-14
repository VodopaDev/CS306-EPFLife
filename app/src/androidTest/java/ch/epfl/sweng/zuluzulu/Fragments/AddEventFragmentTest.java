package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.widget.Spinner;

import org.junit.Rule;
import org.junit.Test;


import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAdminAndFragment;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;

public class AddEventFragmentTest extends TestWithAdminAndFragment<EventFragment> {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Override
    public void initFragment() {
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
        onView(withId(R.id.event_title)).perform(typeText("This is a title much too long to be able to put it on the database"));
        onView(withId(R.id.long_desc_text)).perform(typeText("Okay I am now writing a whole story about the life of this test. So once upon a time, a test was created, it was supposed to be super useful and everyone was happy, until they started it, and it failed. And then started hours and hours of debugging" +
                "continuously until it passed. That was the story, thank you")).perform(closeSoftKeyboard());
        onView(withId(R.id.create_event_button)).perform(click());
        onView(withId(R.id.add_event_layout)).check(matches(isDisplayed()));
    }

    /**
     * change the spinner_month value to the month wanted and check if the spinner_day value is correctly updated
     * @param spinner , the spinner_day
     * @param month , the wanted month
     * @param expectedDay , the day we expect spinner_day to take
     */
    private void checkDayValueAfterMonthChange(Spinner spinner, String month, String expectedDay){
        onView(withId(R.id.spinner_month)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(month))).perform(click());
        assertEquals(expectedDay,spinner.getSelectedItem().toString());
    }

    /**
     * Set the value of Spinner_day to ;
     * Is based on the fact that January or a 31 day months is selected in the spinner_month
     * @param wantedDay , the day we set the spinner to
     */
    private void setSpinnerDay(String wantedDay){
        onView(withId(R.id.spinner_day)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(wantedDay))).perform(click());
    }

    /**
     * used to test limit cases where the day selected is bigger than the new month we select after.
     * Also check that the day doesn't change after we go back to january
     *
     * @param spinner , the day spinner we test the value
     * @param month , the month we want to change to
     * @param dayTested , the value we want to obtain after month change
     */
    private void helperDaySpinnerTest(Spinner spinner, String month, String dayTested){
        setSpinnerDay("31");
        checkDayValueAfterMonthChange(spinner,month, dayTested);
        checkDayValueAfterMonthChange(spinner, "Jan", dayTested);
    }

    /**
     * Test that the value of spinner_day changes accordingly to the month we select
     * (example if 31 january selected, if we select february it goes to 28 and not stay at 31)
     */
    @Test
    public void testDaySpinner(){
        goToAddEvent();
        Spinner spinnertest = mActivityRule.getActivity().findViewById(R.id.spinner_day);

        //check that if the day is 31 that it updates itself to the max of the new month
        helperDaySpinnerTest(spinnertest,"Feb", "28");
        helperDaySpinnerTest(spinnertest,"Nov", "30");

        //Check that the value if it is smaller than 28 never change for any type of month
        setSpinnerDay("5");
        checkDayValueAfterMonthChange(spinnertest, "Feb", "5");
        checkDayValueAfterMonthChange(spinnertest, "Nov", "5");


    }

    /**
     * create an event and controls that it is indeed created in the event list
     */
    /*@Test
    public void testCreateEvent(){
        goToAddEvent();
        onView(withId(R.id.event_title)).perform(typeText("Test Event"));
        onView(withId(R.id.long_desc_text)).perform(typeText("this is an awesome test event")).perform(closeSoftKeyboard());
        onView(withId(R.id.create_event_button)).perform(click());
    }*/
}
