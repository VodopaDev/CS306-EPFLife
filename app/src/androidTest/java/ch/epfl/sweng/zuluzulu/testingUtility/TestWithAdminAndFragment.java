package ch.epfl.sweng.zuluzulu.testingUtility;

import ch.epfl.sweng.zuluzulu.fragments.superFragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.structure.user.Admin;
import ch.epfl.sweng.zuluzulu.Utility;

public abstract class TestWithAdminAndFragment<F extends SuperFragment> extends TestWithUserAndFragment<Admin, F> {

    @Override
    public void initUser() {
        user = Utility.createTestAdmin();
    }

}
