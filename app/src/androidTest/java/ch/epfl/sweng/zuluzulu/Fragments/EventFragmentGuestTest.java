package ch.epfl.sweng.zuluzulu.Fragments;

import org.junit.Before;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithGuestAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;


public class EventFragmentGuestTest extends TestWithGuestAndFragment<EventFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());
        fragment = EventFragment.newInstance(user);
    }

    @Override
    @Before
    public void init(){
        super.init();
        onView(withId(R.id.event_fragment_filter_button)).perform(click());
    }

    @Test
    public void warningWhenGuestClickOnFav(){
        onView(withId(R.id.event_fragment_fav_button)).perform(click());
    }

    @Test
    public void clickOnDisableCheckbox(){
        onView(withId(R.id.event_fragment_checkBox_sort_date)).perform(click());
    }

    @Test
    public void clickOnEnableCheckbox() {
        onView(withId(R.id.event_fragment_checkbox_sort_like)).perform(click());

    }
}
