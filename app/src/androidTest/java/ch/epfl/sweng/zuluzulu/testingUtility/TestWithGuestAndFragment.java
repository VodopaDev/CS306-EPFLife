package ch.epfl.sweng.zuluzulu.testingUtility;

import ch.epfl.sweng.zuluzulu.fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.structure.user.Guest;
import ch.epfl.sweng.zuluzulu.Utility;

public abstract class TestWithGuestAndFragment<F extends SuperFragment> extends TestWithUserAndFragment<Guest, F> {

    @Override
    public void initUser() {
        user = Utility.createTestGuest();
    }
}
