package ch.epfl.sweng.zuluzulu.TestingUtility;

import ch.epfl.sweng.zuluzulu.Fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Utility;

public abstract class TestWithAuthenticatedAndFragment<F extends SuperFragment> extends TestWithUserAndFragment<AuthenticatedUser, F> {

    @Override
    public void initUser() {
        user = Utility.createTestAuthenticated();
    }
}
