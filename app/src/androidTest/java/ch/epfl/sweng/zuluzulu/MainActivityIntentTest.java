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
import static org.hamcrest.core.IsNot.not;

public class MainActivityIntentTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);



    @Test
    public void onCreateGoesToLogin() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        mActivityRule.launchActivity(i);

        onView(withId(R.id.login_form)).check(matches(isDisplayed()));
    }
    @Test
    public void onCreateGoesToMain() {
        Intent i = new Intent();
        mActivityRule.launchActivity(i);

        onView(withId(R.id.main_fragment)).check(matches(isDisplayed()));
    }

}
