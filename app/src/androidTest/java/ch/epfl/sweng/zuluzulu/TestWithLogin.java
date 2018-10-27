package ch.epfl.sweng.zuluzulu;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;

import ch.epfl.sweng.zuluzulu.Structure.User;

/**
 * This class allow us to create the main activity as if we were connected.
 * It creates an user and allow child to use this User.
 *
 * Author @dahn
 */
public abstract class TestWithLogin {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);


    protected User user;

    @Before
    public void setUp() {
        this.user = Utility.createTestUser();
        Utility.addUserToMainIntent(mActivityRule, this.user);
    }

    /**
     * Return the user for the childs
     * @return User
     */
    protected User getUser() {
        return this.user;
    }
}
