package ch.epfl.sweng.zuluzulu;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

/**
 * This class test the MainActivity as a connected User
 */
public class MainActivityAsUserTest extends TestWithLogin {


    @Test
    public void isAuthenticated() {
        // check not authenticated
        assertTrue(mActivityRule.getActivity().isAuthenticated());
        onView(withId(R.id.main_user_fragment)).check(matches(isDisplayed()));
    }
}