package ch.epfl.sweng.zuluzulu;

import android.content.Intent;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    private void openDrawer() {
        // Open Drawer to click on navigation.
        Utility.openMenu();
    }

    @Before
    public void openLoginFragment() {
        openDrawer();

        // Click on the login item in the Drawer
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_login));
    }

    /**
     * Test connection is accepted with correct credentials
     */
    @Test
    public void isOnTheLogin() {

        //You have to test if it works for wrong credentials, if it login properly and if you have any
        //other idea you are welcome to test them
        onView(withId(R.id.login_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void canSeetheWebView() throws InterruptedException {
        TimeUnit.SECONDS.sleep(4);
        onView(withId(R.id.sign_in_button)).perform(click());
        TimeUnit.SECONDS.sleep(3);
        onView(withId(R.id.webview)).check(matches(isDisplayed()));
    }

    @Test
    public void errorIfReceivesAWrongRedirectUri() throws Exception {
        Intent intent = new Intent();
        intent.putExtra("redirectUri", "blablablaIHavecode=1234");
        mActivityRule.launchActivity(intent);
        onView(withId(R.id.login_fragment)).check(matches(isDisplayed()));
    }

}
