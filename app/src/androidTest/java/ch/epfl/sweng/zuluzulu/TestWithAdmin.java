package ch.epfl.sweng.zuluzulu;

import org.junit.Before;

import ch.epfl.sweng.zuluzulu.database.MockedProxy;
import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.user.Admin;

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
