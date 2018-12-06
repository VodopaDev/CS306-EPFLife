package ch.epfl.sweng.zuluzulu.LocalDatabase;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.Utility;

import static com.google.common.base.CharMatcher.isNot;
import static org.junit.Assert.*;

public class UserDatabaseTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void TestAll(){
        UserDatabase db = new UserDatabase(mActivityRule.getActivity().getApplicationContext());
        assert(db.put(Utility.createTestAuthenticated()) >= 0);
        db.getUser();
        assert(db.delete(Utility.createTestAuthenticated()) >= 0);
    }

}