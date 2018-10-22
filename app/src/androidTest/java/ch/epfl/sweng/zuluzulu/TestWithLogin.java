package ch.epfl.sweng.zuluzulu;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;

import ch.epfl.sweng.zuluzulu.Structure.User;

public abstract class TestWithLogin {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);


    private User user;

    @Before
    public void setUp() {
        user = Utility.createTestUser();
        Utility.addUserToMainIntent(mActivityRule, user);
    }

    public User getUser(){
        return this.user;
    }
}
