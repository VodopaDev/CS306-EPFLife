package ch.epfl.sweng.zuluzulu;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * This class is to test if the MainActivity starts the right fragment depending on the intent
 * input
 */
public class MainActivityIntentTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);


    /**
     * Test if the app start on the login with an redirectUri intent
     */
    @Test
    public void intentRedirectLogin() {
        Intent intent = new Intent();
        intent.putExtra("redirectUri", "blablablaIHavecode=1234");
        mActivityRule.launchActivity(intent);

        Utility.checkFragmentIsOpen(R.id.login_fragment);
    }

    /**
     * Check if it goes to the main
     */
    @Test
    public void onCreateGoesToMain() {
        Intent i = new Intent();
        mActivityRule.launchActivity(i);

        onView(withId(R.id.main_fragment)).check(matches(isDisplayed()));
    }
}
