package ch.epfl.sweng.zuluzulu;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.LocalDatabase.UserDatabase;
import ch.epfl.sweng.zuluzulu.LocalDatabase.UserDatabaseContract;
import ch.epfl.sweng.zuluzulu.LocalDatabase.UserDatabaseHelper;
import ch.epfl.sweng.zuluzulu.User.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * This class is to test if the MainActivity starts the right fragment depending on the intent
 * input
 */
public class MainActivityIntentTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);


    /**
     * Test if the app start on the login with an redirectUri intent
     */
    @Test
    public void intentRedirectLogin() {
        DatabaseFactory.setDependency(new MockedProxy());


        Intent intent = new Intent();
        intent.putExtra("redirectUri", "blablablaIHavecode=1234");
        mActivityRule.launchActivity(intent);

        Utility.checkFragmentIsOpen(R.id.login_fragment);
    }
}
