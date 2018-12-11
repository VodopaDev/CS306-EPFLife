package ch.epfl.sweng.zuluzulu;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Fragments.LoginFragment;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithGuestAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginTest extends TestWithGuestAndFragment<LoginFragment> {

    private void openDrawer() {
        // Open Drawer to click on navigation.
        Utility.openMenu();
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


    @Override
    public void initFragment() {
        fragment = LoginFragment.newInstance();
        DatabaseFactory.setDependency(new MockedProxy());
    }
}
