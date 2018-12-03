package ch.epfl.sweng.zuluzulu;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.Fragments.EventFragment;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedAndFragment;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
public class EventDetailFragmentTest extends TestWithAuthenticatedAndFragment<EventFragment> {

    private static final String FAV_CONTENT = "This event is in your favorites";
    private static final String NOT_FAV_CONTENT = "This event isn't in your favorites";

    @Override
    public void initFragment() {
        fragment = EventFragment.newInstance(user);
    }

<<<<<<< HEAD
    @Test
    public void mainTest() {

    }
}
/*    private void guestGoesToEventDetail() throws InterruptedException {
        Utility.goToEvent();
        TimeUnit.SECONDS.sleep(1);
=======
    public void waitABit(){
        try{
            Thread.sleep(5000);
        }catch (Exception e){
            System.out.println("error in waiting");
        }
>>>>>>> 8a2ab7225df573bcd26d0ca428e6f0eb0e6c0228
    }

    @Test
    @Ignore
    public void authenticatedCanOpenAnEvent(){
        waitABit();
        onData(anything()).inAdapterView(withId(R.id.event_fragment_listview)).atPosition(0).perform(click());
        // onView(withId(R.id.event_detail_fav)).check(matches(isDisplayed()));
    }

//   @Test
//    public void authenticatedCanOpenTheChatOfAnEvent() {
//       waitABit();
//       onData(anything()).inAdapterView(withId(R.id.event_fragment_listview)).atPosition(0).perform(click());
//       onView(withId(R.id.event_detail_chatRoom)).perform(click());
//       onView(withId(R.id.chat_send_button)).check(matches(isDisplayed()));
//    }

    @Test
    public void authenticatedCanOpenTheAssociationOfAnEvent() {
        // waitABit();
        // onData(anything()).inAdapterView(withId(R.id.event_fragment_listview)).atPosition(0).perform(click());
        // onView(withId(R.id.event_detail_but_assos)).perform(click());
        // Utility.checkFragmentIsOpen(R.id.association_detail_fragment);
    }
}
