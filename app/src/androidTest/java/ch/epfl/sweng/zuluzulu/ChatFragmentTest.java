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
public class ChatFragmentTest extends TestWithLogin {

    private static final int channelID = 1;

    private Fragment fragment;

    @Before
    public void init() {
        fragment = ChatFragment.newInstance(getUser(), channelID);
        mActivityRule.getActivity().openFragment(fragment);
    }


    @Test
    public void testUserCanSendAMessageAndReadIt() {
        /* Todo
        I don't understand how to test this without the dependence of firestore
        I don't want to write a message in the database during the test
        How can I mock the behavior of firestore..?
         */

        // Type message
        //onView(withId(R.id.chat_message_edit)).perform(replaceText(MSG));

        //onView(withId(R.id.chat_send_button)).perform(click());
    }
}
