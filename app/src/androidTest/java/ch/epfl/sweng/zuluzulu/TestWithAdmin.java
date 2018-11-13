package ch.epfl.sweng.zuluzulu;

import org.junit.Before;

import ch.epfl.sweng.zuluzulu.Structure.Admin;

public class TestWithAdmin extends TestWithAuthenticatedUser {

    private Admin admin;

    @Before
    public void setUpLogin() {
        this.admin = Utility.createTestAdmin();
        Utility.addUserToMainIntent(mActivityRule, this.admin);
    }

    @Override
    protected Admin getUser() {
        return admin;
    }
}
