package ch.epfl.sweng.zuluzulu;

import org.junit.Before;

public class TestWithAdminLogin extends TestWithLogin {
    @Before
    public void setUp() {
        this.user = Utility.createTestAdmin();
        Utility.addUserToMainIntent(mActivityRule, this.user);
    }
}
