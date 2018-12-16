package ch.epfl.sweng.zuluzulu.Fragments;

import android.app.Activity;
import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.v4.content.ContextCompat;
import android.widget.ImageButton;

import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import ch.epfl.sweng.zuluzulu.BitmapUtils;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAdminAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;

import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertTrue;


public class ProfileFragmentTest extends TestWithAdminAndFragment<ProfileFragment> {
    private Bitmap picture;

    @Rule
    public final IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule<>(MainActivity.class);

    @Override
    public void initFragment() {

        DatabaseFactory.setDependency(new MockedProxy());
        fragment = ProfileFragment.newInstance(user, true);

        Drawable draw = ContextCompat.getDrawable(mActivityRule.getActivity(), R.drawable.ic_add_circle_red);

        picture = drawableToBitmap(draw);

        File directory = mActivityRule.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File (directory + "/user0" + ".jpg");
        String path = image.getAbsolutePath();

        BitmapUtils.writeBitmapInSDCard(picture, path);
    }

    private Bitmap drawableToBitmap(Drawable draw){
        int width = draw.getIntrinsicWidth();
        int height = draw.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        draw.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        draw.draw(canvas);
        return bitmap;
    }

    @Test
    public void checkName() {
        onView(ViewMatchers.withId(R.id.profile_gaspar_text)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void checkPicture() {
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, null);

        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);

        onView(ViewMatchers.withId(R.id.profile_image)).perform(click());

        ImageButton pic = mActivityRule.getActivity().findViewById(R.id.profile_image);


        Bitmap obtained = drawableToBitmap(pic.getDrawable());

        assertTrue(obtained.getWidth() == picture.getWidth() && obtained.getHeight() == picture.getHeight());
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