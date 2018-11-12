package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.contrib.NavigationViewActions;
import org.junit.Before;
import org.junit.Test;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class AddEventFragmentTest extends TestWithAdminLogin{

    @Before
    public void init(){
        Utility.goToEvent();
        onView(withId(R.id.event_add_button)).perform(click());
    }

    @Test
    public void testCanOpenAssociationsGenerator() {
        onView(withId(R.id.event_title)).perform(typeText("Test Event")).perform(closeSoftKeyboard());
        onView(withId(R.id.long_desc_text)).perform(typeText("this is a description for a test event")).perform(closeSoftKeyboard());
        onView(withId(R.id.create_event_button)).perform(click());
    }
}
