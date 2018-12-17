package ch.epfl.sweng.zuluzulu.localDatabase;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.Utility;

public class UserDatabaseTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void TestAll() {
        UserDatabase db = new UserDatabase(mActivityRule.getActivity().getApplicationContext());
        db.put(Utility.createTestAuthenticated());
        db.getUser();
        assert (db.delete(Utility.createTestAuthenticated()) >= 0);
    }

}