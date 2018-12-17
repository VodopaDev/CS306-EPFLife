package ch.epfl.sweng.zuluzulu.testingUtility;

import ch.epfl.sweng.zuluzulu.fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.structure.user.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Utility;

public abstract class TestWithAuthenticatedAndFragment<F extends SuperFragment> extends TestWithUserAndFragment<AuthenticatedUser, F> {

    @Override
    public void initUser() {
        user = Utility.createTestAuthenticated();
    }
}
