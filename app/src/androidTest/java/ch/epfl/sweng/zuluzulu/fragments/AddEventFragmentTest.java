package ch.epfl.sweng.zuluzulu.fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.fragments.adminFragments.AddEventFragment;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.database.MockedProxy;
import ch.epfl.sweng.zuluzulu.testingUtility.TestWithAdminAndFragment;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class AddEventFragmentTest extends TestWithAdminAndFragment<AddEventFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());
        fragment = AddEventFragment.newInstance();
    }


    /**
     * Test if both fields Title and Description are left empty
     */
    @Test
    public void testEmptyTitleAndDesc() {
        onView(withId(R.id.create_event_button)).perform(click());

        onView(withId(R.id.add_event_fragment)).check(matches(isDisplayed()));
    }

    /**
     * test different possibilities of putting wrong dates on the calendar
     */
    @Test
    public void testWithWrongDate(){
        onView(withId(R.id.event_title)).perform(replaceText("Test Event"));
        onView(withId(R.id.long_desc_text)).perform(replaceText("this is an awesome test event")).perform(closeSoftKeyboard());

        onView(withId(R.id.date_for_add)).perform(PickerActions.setDate(2010, 1, 1));
        onView(withId(R.id.create_event_button)).perform(click());
        onView(withId(R.id.add_event_fragment)).check(matches(isDisplayed()));

        onView(withId(R.id.end_date_for_add)).perform(ViewActions.scrollTo());

        onView(withId(R.id.end_date_for_add)).perform(PickerActions.setDate(2000, 1, 1));
        onView(withId(R.id.create_event_button)).perform(ViewActions.scrollTo());
        onView(withId(R.id.create_event_button)).perform(click());
        onView(withId(R.id.add_event_fragment)).check(matches(isDisplayed()));



    }

    /**
     * test that create a complete event with all fields and correct dates doesn't crash
     */
    @Test
    public void testCreateCompleteEvent(){
        onView(withId(R.id.event_title)).perform(replaceText("Test Event"));
        onView(withId(R.id.long_desc_text)).perform(replaceText("this is an awesome test event")).perform(closeSoftKeyboard());
        onView(withId(R.id.date_for_add)).perform(ViewActions.scrollTo()).perform(PickerActions.setDate(2019, 1, 1));
        onView(withId(R.id.end_date_for_add)).perform(ViewActions.scrollTo()).perform(PickerActions.setDate(2019, 2, 1));
        onView(withId(R.id.organizer)).perform(ViewActions.scrollTo()).perform(replaceText("test organizer")).perform(closeSoftKeyboard());
        onView(withId(R.id.category)).perform(ViewActions.scrollTo()).perform(replaceText("test category")).perform(closeSoftKeyboard());
        onView(withId(R.id.contact)).perform(ViewActions.scrollTo()).perform(replaceText("test contact")).perform(closeSoftKeyboard());
        onView(withId(R.id.place)).perform(ViewActions.scrollTo()).perform(replaceText("CO 1")).perform(closeSoftKeyboard());
        onView(withId(R.id.speaker)).perform(ViewActions.scrollTo()).perform(replaceText("test speaker")).perform(closeSoftKeyboard());
        onView(withId(R.id.website)).perform(ViewActions.scrollTo()).perform(replaceText("http://google.com")).perform(closeSoftKeyboard());
        onView(withId(R.id.create_event_button)).perform(ViewActions.scrollTo()).perform(click());
    }

    /**
     * create the bare minimum for an event and see if it still accepts
     */
    @Test
    public void testCreateMinEvent() {
        onView(withId(R.id.event_title)).perform(replaceText("Test Event"));
        onView(withId(R.id.long_desc_text)).perform(replaceText("this is an awesome test event")).perform(closeSoftKeyboard());
        onView(withId(R.id.date_for_add)).perform(ViewActions.scrollTo()).perform(PickerActions.setDate(2019, 1, 1));
        onView(withId(R.id.end_date_for_add)).perform(ViewActions.scrollTo()).perform(PickerActions.setDate(2019, 2, 1));
        onView(withId(R.id.create_event_button)).perform(ViewActions.scrollTo()).perform(click());
    }
}
