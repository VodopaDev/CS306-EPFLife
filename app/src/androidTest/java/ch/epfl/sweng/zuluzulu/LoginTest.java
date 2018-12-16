package ch.epfl.sweng.zuluzulu;

import android.os.Bundle;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Fragments.LoginFragment;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithGuestAndFragment;

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
     * Test connection is refuesd with bad credentials
     * Actually just test the app doesn't crash.
     */
    @Test
    public void isUrlRedirectedToMain() {
        Utility.openMenu();
    }

}
