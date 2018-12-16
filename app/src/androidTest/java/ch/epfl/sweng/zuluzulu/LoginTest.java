package ch.epfl.sweng.zuluzulu;

import android.os.Bundle;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Fragments.LoginFragment;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithGuestAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.web.sugar.Web.onWebView;

@RunWith(AndroidJUnit4.class)
public class LoginTest extends TestWithGuestAndFragment<LoginFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());
        Bundle toSend = new Bundle(1);
        toSend.putString("uri", "http://epfl.ch/code=1234");
        fragment = LoginFragment.newInstance();
        fragment.setArguments(toSend);
    }

    /**
     * Test connection is accepted with correct credentials
     */
    @Test
    public void isOnTheLogin() {
        onView(withId(R.id.login_fragment)).check(matches(isDisplayed()));
    }

}
