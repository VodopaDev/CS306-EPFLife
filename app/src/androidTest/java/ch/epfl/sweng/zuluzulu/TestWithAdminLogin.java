package ch.epfl.sweng.zuluzulu;

import org.junit.Before;

import ch.epfl.sweng.zuluzulu.Structure.Admin;
import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.User;

public class TestWithAdminLogin extends TestWithLogin {

    private Admin admin;

    @Before
    public void setUpLogin() {
        this.admin = (Admin)Utility.createTestAdmin();
        Utility.addUserToMainIntent(mActivityRule, this.admin);
    }

    @Override
    protected Admin getUser() {
        return admin;
    }
}
