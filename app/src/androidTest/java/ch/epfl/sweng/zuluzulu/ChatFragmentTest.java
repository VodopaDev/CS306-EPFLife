package ch.epfl.sweng.zuluzulu;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.Fragments.ChatFragment;
import ch.epfl.sweng.zuluzulu.Fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedUser;

@RunWith(AndroidJUnit4.class)
public class ChatFragmentTest extends TestWithAuthenticatedUser {

    private static Channel channel;

    private SuperFragment fragment;

    @Before
    public void init() {
        Map data = new HashMap();
        data.put("id", 1l);
        data.put("name", "name");
        data.put("description", "description");
        data.put("restrictions", new HashMap<>());
        FirebaseMapDecorator fmap = new FirebaseMapDecorator(data);
        channel = new Channel(fmap);
        fragment = ChatFragment.newInstance(getUser(), channel);
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
