package ch.epfl.sweng.zuluzulu.TestingUtility;

import ch.epfl.sweng.zuluzulu.Fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.User.Admin;
import ch.epfl.sweng.zuluzulu.Utility;

public abstract class TestWithAdminAndFragment<F extends SuperFragment> extends TestWithUserAndFragment<Admin, F> {

    @Override
    public void initUser(){
        user = Utility.createTestAdmin();
    }

}