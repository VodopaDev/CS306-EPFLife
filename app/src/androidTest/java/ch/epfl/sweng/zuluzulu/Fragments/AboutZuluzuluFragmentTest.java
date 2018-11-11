package ch.epfl.sweng.zuluzulu.Fragments;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class AboutZuluzuluFragmentTest extends TestWithAuthenticatedAndFragment<AboutZuluzuluFragment> {

    @Override
    public void initFragment() {
        fragment = AboutZuluzuluFragment.newInstance();
    }

    @Test
    public void canSendEmail() {
        // Use this to ignore the request
        Intent resultData = new Intent();
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(anyIntent()).respondWith(result);

        onView(withId(R.id.send_mail)).perform(click());

    }
}