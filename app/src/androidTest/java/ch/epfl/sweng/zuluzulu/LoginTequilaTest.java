package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class LoginTequilaTest {

    @Rule
    public final ActivityTestRule<LoginTequila> mActivityRule =
            new ActivityTestRule<>(LoginTequila.class);

    /**
     *
     * Test connection is accepted with correct credentials
     */
    @Test
    public void testCanLogIn() {
        //You have to test if it works for wrong credentials, if it logins properly and if you have any
        //other idea you are welcome to test them

        onView(withId(R.id.email)).perform(typeText("user@epfl.ch")).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("password")).perform(closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withId(R.id.logoutButton)).check(matches(isDisplayed()));

    }

    /**
     * Test connection is refused with bad credentials
     */
    @Test
    public void testWrongLogIn() {
        //You have to test if it works for wrong credentials, if it logins properly and if you have any
        //other idea you are welcome to test them

        onView(withId(R.id.email)).perform(typeText("not_user@epfl.ch")).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("password")).perform(closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click()).check(matches(isDisplayed()));


        onView(withId(R.id.email)).perform(replaceText("user@epfl.ch")).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(replaceText("wrong_password")).perform(closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click()).check(matches(isDisplayed()));
    }

    /**
     * Check if the mail is correct (@epfl.ch)
     */
    @Test
    public void acceptOnlyEPFL(){
        onView(withId(R.id.email)).perform(typeText("user@epfl.ch")).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(replaceText("wrong_password")).perform(closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withId(R.id.email)).check(matches(hasNoErrorText()));

        onView(withId(R.id.email)).perform(replaceText("user@not_epfl.ch")).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(replaceText("wrong_password")).perform(closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withId(R.id.email)).check(matches(not(hasNoErrorText())));
    }


    /**
     * Check if a wrong password return an error message
     */
    @Test
    public void checkPasswords(){
        onView(withId(R.id.email)).perform(replaceText("user@epfl.ch")).perform(closeSoftKeyboard());
        // Test with a too small password
        onView(withId(R.id.password)).perform(replaceText("wra")).perform(closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withId(R.id.password)).check(matches(not(hasNoErrorText())));
    }

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
}
