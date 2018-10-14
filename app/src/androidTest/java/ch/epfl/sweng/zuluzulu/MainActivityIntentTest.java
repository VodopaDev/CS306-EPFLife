package ch.epfl.sweng.zuluzulu;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class MainActivityIntentTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);


    /**
     * Test if the app start on the login with an ACTION_VIEW intent
     */
    @Test
    public void onCreateGoesToLogin() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        mActivityRule.launchActivity(i);

        onView(withId(R.id.login_fragment)).check(matches(isDisplayed()));
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
