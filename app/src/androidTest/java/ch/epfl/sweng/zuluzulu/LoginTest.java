package ch.epfl.sweng.zuluzulu;

import android.support.design.widget.NavigationView;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class LoginTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    /**
     * Class test if there is an error message in a EditText
     */
    private static Matcher<View> hasNoErrorText() {
        return new BoundedMatcher<View, EditText>(EditText.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("has no error text: ");
            }

            @Override
            protected boolean matchesSafely(EditText view) {
                return view.getError() == null;
            }
        };
    }

    private void openDrawer(){
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
    public void testCanLogIn() {
        //You have to test if it works for wrong credentials, if it logins properly and if you have any
        //other idea you are welcome to test them
        Utility.login();

        openDrawer();
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_logout));
    }
    /**
     * Test connection is refused with bad credentials
     */
    @Test
    public void testWrongLogIn() {
        //You have to test if it works for wrong credentials, if it logins properly and if you have any
        //other idea you are welcome to test them

        onView(withId(R.id.username)).perform(typeText("not_user")).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("password")).perform(closeSoftKeyboard());
        onView(withId(R.id.sign_in_button)).perform(click()).check(matches(isDisplayed()));

        onView(withId(R.id.username)).perform(replaceText("user")).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(replaceText("wrong_password1")).perform(closeSoftKeyboard());
        onView(withId(R.id.sign_in_button)).perform(click()).check(matches(isDisplayed()));
    }

    /**
     * Check if the username is correct (refuse too short)
     */
    @Test
    public void checkUserName() {
        // too short
        onView(withId(R.id.username)).perform(replaceText("")).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(replaceText("wrong_password2")).perform(closeSoftKeyboard());
        onView(withId(R.id.sign_in_button)).perform(click());
        onView(withId(R.id.username)).check(matches(not(hasNoErrorText())));
    }

    /**
     * Check if a wrong password return an error message
     */
    @Test
    public void checkPasswords() {
        // Test with a too small password
        onView(withId(R.id.username)).perform(typeText("username")).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("wra")).perform(closeSoftKeyboard());
        onView(withId(R.id.sign_in_button)).perform(click());
        onView(withId(R.id.password)).check(matches(not(hasNoErrorText())));
    }
}
