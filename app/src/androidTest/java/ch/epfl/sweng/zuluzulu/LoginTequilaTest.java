package ch.epfl.sweng.zuluzulu;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginTequilaTest {

    @Rule
    public final ActivityTestRule<LoginTequila> mActivityRule =
            new ActivityTestRule<>(LoginTequila.class);
    @Test
    public void testCanLogIn() {
        //You have to test if it works for wrong credentials, if it logins properly and if you have any
        //other idea you are welcome to test them
    }
}
