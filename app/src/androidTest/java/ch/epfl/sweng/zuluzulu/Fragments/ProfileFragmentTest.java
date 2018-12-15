package ch.epfl.sweng.zuluzulu.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.GrantPermissionRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Utility;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;

public class ProfileFragmentTest {
    @Rule
    public final IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA);


    private AuthenticatedUser user;

    @Before
    public void init() {
        user = Utility.createTestAdmin();
        SuperFragment fragment = ProfileFragment.newInstance(user, true);
        mActivityRule.getActivity().openFragment(fragment);
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

        onView(ViewMatchers.withId(R.id.profile_add_photo)).perform(click());
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