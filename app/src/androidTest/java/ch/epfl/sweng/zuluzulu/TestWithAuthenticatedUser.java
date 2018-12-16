package ch.epfl.sweng.zuluzulu;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;

/**
 * This class allow us to create the main activity as if we were connected.
 * It creates an user and allow child to use this User.
 * <p>
 * Author @dahn
 */
public abstract class TestWithAuthenticatedUser {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);


    private AuthenticatedUser user;

    @Before
    public void setUpLogin() {
        this.user = Utility.createTestAuthenticated();
        Utility.addUserToMainIntent(mActivityRule, this.user);

        DatabaseFactory.setDependency(new MockedProxy());
    }

    /**
     * Return the user for the childs
     *
     * @return User
     */
    protected AuthenticatedUser getUser() {
        return this.user;
    }
}
