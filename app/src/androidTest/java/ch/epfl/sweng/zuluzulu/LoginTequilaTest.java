package ch.epfl.sweng.zuluzulu;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

@RunWith(AndroidJUnit4.class)
public class LoginTequilaTest {

    @Rule
    public final ActivityTestRule<LoginTequila> mActivityRule =
            new ActivityTestRule<>(LoginTequila.class);

    @Test
    public void testCanLogIn() {
        //You have to test if it works for wrong credentials, if it logins properly and if you have any
        //other idea you are welcome to test them

        onView(withId(R.id.email)).perform(typeText("user@epfl.ch")).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("password")).perform(closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withId(R.id.logoutButton)).check(matches(isDisplayed()));

    }


}
