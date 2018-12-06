package ch.epfl.sweng.zuluzulu;

import org.junit.Before;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.User.Admin;

public class TestWithAdmin extends TestWithAuthenticatedUser {

    private Admin admin;

    @Before
    public void setUpLogin() {
        this.admin = Utility.createTestAdmin();
        Utility.addUserToMainIntent(mActivityRule, this.admin);

        DatabaseFactory.setDependency(new MockedProxy());
    }

    @Override
    protected Admin getUser() {
        return admin;
    }
}
