package ch.epfl.sweng.zuluzulu.TestingUtility;

import ch.epfl.sweng.zuluzulu.Fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.User.Guest;
import ch.epfl.sweng.zuluzulu.Utility;

public abstract class TestWithGuestAndFragment<F extends SuperFragment> extends TestWithUserAndFragment<Guest, F> {

    @Override
    public void initUser() {
        user = Utility.createTestGuest();
    }
}
