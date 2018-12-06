package ch.epfl.sweng.zuluzulu;

import org.junit.Before;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.User.Admin;

public class TestWithAdmin extends TestWithAuthenticatedUser {

    private Admin admin;

    @Before
    public void setUpLogin() {
        DatabaseFactory.setDependency(new MockedProxy());

        this.admin = Utility.createTestAdmin();
        Utility.addUserToMainIntent(mActivityRule, this.admin);
    }

    @Override
    protected Admin getUser() {
        return admin;
    }
}
