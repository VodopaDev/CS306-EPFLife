package ch.epfl.sweng.zuluzulu;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Fragments.ChatFragment;
import ch.epfl.sweng.zuluzulu.Structure.User;

@RunWith(AndroidJUnit4.class)
public class ChatFragmentTest {

    private User user;
    private Fragment fragment;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
        User.UserBuilder builder = new User.UserBuilder();
        builder.setSciper("123456");
        builder.setGaspar("gaspar");
        builder.setEmail("test@epfl.ch");
        builder.setFirst_names("james");
        builder.setLast_names("bond");
        user = builder.buildAuthenticatedUser();

        fragment = ChatFragment.newInstance(user, 1);
        mActivityRule.getActivity().openFragment(fragment);
    }

    @Test
    public void testUserCanSendAMessageAndReadIt() {
        /* Todo
        I don't understand how to test this without the dependence of firestore
        I don't want to write a message in the database during the test
        How can I mock the behavior of firestore..?
         */
    }
}
