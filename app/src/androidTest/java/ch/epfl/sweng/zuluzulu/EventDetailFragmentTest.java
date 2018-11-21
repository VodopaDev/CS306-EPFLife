package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.Fragments.AssociationFragment;
import ch.epfl.sweng.zuluzulu.Fragments.EventDetailFragment;
import ch.epfl.sweng.zuluzulu.Fragments.EventFragment;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedAndFragment;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class EventDetailFragmentTest extends TestWithAuthenticatedAndFragment<EventFragment> {

    private static final String FAV_CONTENT = "This event is in your favorites";
    private static final String NOT_FAV_CONTENT = "This event isn't in your favorites";

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Override
    public void initFragment() {
        fragment = EventFragment.newInstance(user);
    }
    /*private void guestGoesToEventDetail() throws InterruptedException {
        Utility.goToEvent();
        TimeUnit.SECONDS.sleep(1);
    }

    private void authenticatedGoesToEvent() throws InterruptedException {
        Utility.fullLogin();
        Utility.goToEvent();
        TimeUnit.SECONDS.sleep(1);
    }

    public void EventDetailFragmentIsOpen() {
        Utility.checkFragmentIsOpen(1);
    }
}
*/  public void waitABit(){
        try{
            Thread.sleep(3000);
        }catch (Exception e){
            System.out.println("error in waiting");
        }
    }


    @Test
    public void authenticatedCanOpenAnEvent(){
        waitABit();
        onView(withText("ForumEPFL")).perform(ViewActions.click());
        onView(withId(R.id.event_detail_fav))
                .check(matches(isDisplayed()));
    }

    /*@Test
    public void guestCanClickOnFavorite() {
        onView(withText("ForumEPFL")).perform(ViewActions.click());
        onView(withContentDescription(NOT_FAV_CONTENT))
                .check(matches(isDisplayed()))
                .perform(ViewActions.click());
        onView(withContentDescription(NOT_FAV_CONTENT))
                .check(matches(isDisplayed()));
    }*/
   @Test
    public void authenticatedCanOpenTheChatOfAnEvent() {
        waitABit();
        onView(withText("Hacking Contest!")).perform(ViewActions.click());
        onView(withId(R.id.event_detail_chatRoom)).perform(ViewActions.click());
        onView(withId(R.id.chat_send_button)).check(matches(isDisplayed()));
    }

    @Test
    public void authenticatedCanOpenTheAssociationOfAnEvent() {
        waitABit();
        onView(withText("Hacking Contest!")).perform(ViewActions.click());
        onView(withId(R.id.event_detail_but_assos)).perform(ViewActions.click());
        Utility.checkFragmentIsOpen(R.id.association_detail_fragment);
    }

}
