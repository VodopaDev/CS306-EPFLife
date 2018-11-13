package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.widget.Spinner;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

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

public class AddEventFragmentTest extends TestWithAdminLogin{

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){
        Utility.goToEvent();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.event_add_button)).perform(click());
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test if both fields Title and Description are left empty
     */
    @Test
    public void testEmptyTitleAndDesc() {
        onView(withId(R.id.create_event_button)).perform(click());
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.add_event_layout)).check(matches(isDisplayed()));
    }

    /**
     * Test if both fields Title and Description have too much text
     */
    @Test
    public void testTooManyCharactersInBoth(){
        onView(withId(R.id.event_title)).perform(typeText("This is a title much too long to be able to put it on the database"));
        onView(withId(R.id.long_desc_text)).perform(typeText("Okay I am now writing a whole story about the life of this test. So once upon a time, a test was created, it was supposed to be super useful and everyone was happy, until they started it, and it failed. And then started hours and hours of debugging" +
                "continuously until it passed. That was the story, thank you")).perform(closeSoftKeyboard());
        onView(withId(R.id.create_event_button)).perform(click());
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.add_event_layout)).check(matches(isDisplayed()));
    }

    /**
     * change the spinner_month value to the month wanted and check if the spinner_day value is correctly updated
     * @param spinner , the spinner_day
     * @param month , the wanted month
     * @param expectedDay , the day we expect spinner_day to take
     */
    private void helperSpinnerTest(Spinner spinner, String month, String expectedDay){
        onView(withId(R.id.spinner_month)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(month))).perform(click());
        assertEquals(expectedDay,spinner.getSelectedItem().toString());
    }

    /**
     * Set the value of Spinner_day to 31;
     * Is based on the fact that January or a 31 day months is selected in the spinner_month
     */
    private void resetSpinnerDay(){
        onView(withId(R.id.spinner_day)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("31"))).perform(click());
    }

    /**
     * Test that the value of spinner_day changes accordingly to the month we select
     * (example if 31 january selected, if we select february it goes to 28 and not stay at 31)
     */
    @Test
    public void testDaySpinner(){
        Spinner spinnertest = mActivityRule.getActivity().findViewById(R.id.spinner_day);

        resetSpinnerDay();
        helperSpinnerTest(spinnertest,"Feb", "28");
        helperSpinnerTest(spinnertest, "Jan", "28");

        resetSpinnerDay();
        helperSpinnerTest(spinnertest, "Nov", "30");
        helperSpinnerTest(spinnertest, "Jan", "30");
    }
}
