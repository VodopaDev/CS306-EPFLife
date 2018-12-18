package ch.epfl.sweng.zuluzulu.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import org.junit.Rule;
import org.junit.Test;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.intent.IntentCallback;
import android.support.test.runner.intent.IntentMonitorRegistry;
import org.junit.Before;
import java.io.IOException;
import java.io.OutputStream;
import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.structure.user.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Utility;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;



public class ProfileFragmentTest {
    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA);

    @Rule
    public GrantPermissionRule permissionRule1 = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);

    @Rule
    public GrantPermissionRule permissionRule2 = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);


    private AuthenticatedUser user;


    @Before
    public void init() {
        user = Utility.createTestAdmin();
        SuperFragment fragment = ProfileFragment.newInstance(user, true);
        intentsTestRule.getActivity().openFragment(fragment);

    }

    @Test
    public void checkName() {
        onView(ViewMatchers.withId(R.id.profile_gaspar_text)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void checkPicture() {// Build the result to return when the activity is launched.
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(
                new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

        IntentCallback intentCallback = intent -> {
            if (intent.getAction().equals("android.media.action.IMAGE_CAPTURE")) {
                try {
                    Uri imageUri = intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
                    Context context = getTargetContext();
                    Bitmap icon = BitmapFactory.decodeResource(
                            context.getResources(),
                            R.drawable.default_icon);
                    OutputStream out = getTargetContext().getContentResolver().openOutputStream(imageUri);
                    icon.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    throw new IllegalArgumentException();
                }
            }
        };
        IntentMonitorRegistry.getInstance().addIntentCallback(intentCallback);

        onView(withId(R.id.profile_add_photo)).perform(click());
        Utility.checkFragmentIsOpen(R.id.profile_fragment);

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