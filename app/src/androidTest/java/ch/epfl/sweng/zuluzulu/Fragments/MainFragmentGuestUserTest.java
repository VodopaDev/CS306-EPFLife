package ch.epfl.sweng.zuluzulu.Fragments;

import org.junit.Test;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithGuestAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MainFragmentGuestUserTest extends TestWithGuestAndFragment<MainFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());
        fragment = MainFragment.newInstance(user);
    }

    @Test
    public void theTextsAreDisplayed() {
//        onView(withId(R.id.main_fragment_text_to)).check(matches(isDisplayed()));
//        onView(withId(R.id.main_fragment_text_welcome)).check(matches(isDisplayed()));
//        onView(withId(R.id.main_fragment_guest_image)).check(matches(isDisplayed()));
    }
}
