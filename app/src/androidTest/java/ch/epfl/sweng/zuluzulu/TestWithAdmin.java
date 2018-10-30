package ch.epfl.sweng.zuluzulu;

import org.junit.Before;

import ch.epfl.sweng.zuluzulu.Structure.User;

public class TestWithAdmin extends TestWithAuthenticated {

    private User admin;

    @Before
    public void setUpLogin() {
        this.admin = Utility.createTestAdmin();
        Utility.addUserToMainIntent(mActivityRule, this.admin);
    }

    @Override
    protected User getUser() {
        return admin;
    }
}
