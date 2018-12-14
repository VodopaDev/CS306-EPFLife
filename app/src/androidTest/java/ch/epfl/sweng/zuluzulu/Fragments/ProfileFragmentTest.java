package ch.epfl.sweng.zuluzulu.Fragments;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAdminAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;

public class ProfileFragmentTest extends TestWithAdminAndFragment<ProfileFragment> {
    @Rule
    public final IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule<>(MainActivity.class);

    @Override
    public void initFragment() {

        DatabaseFactory.setDependency(new MockedProxy());
        fragment = ProfileFragment.newInstance(user, true);
    }

    @Test
    public void checkName() {
        onView(ViewMatchers.withId(R.id.profile_gaspar_text)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void checkPicture() {
        Intent resultData = new Intent();
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        intending(anyIntent()).respondWith(result);

        onView(ViewMatchers.withId(R.id.profile_name_text)).perform(click());
    }

    @Test
    public void checkGaspar() {
        onView(withId(R.id.profile_gaspar_text)).check(matches(withText(containsString(user.getGaspar()))));
    }

    @Test
    public void checkEmail() {
        onView(withId(R.id.profile_email_edit)).check(matches(withText(containsString(user.getEmail()))));
    }

    @Test
    public void checkSciper() {
        onView(withId(R.id.profile_sciper_edit)).check(matches(withText(containsString(user.getSciper()))));
    }

    @Test
    public void checkUnit() {
        onView(withId(R.id.profile_unit_edit)).check(matches(withText(containsString(user.getSection()))));
        onView(withId(R.id.profile_unit_edit)).check(matches(withText(containsString(user.getSemester()))));
    }

    @Test
    public void checkAdmin() {
        onView(withId(R.id.profile_name_text)).check(matches(withText(containsString("ADMIN"))));
    }
}