package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ch.epfl.sweng.zuluzulu.Structure.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ChatFragmentTest {

    private static final String MSG = "HELLO FROM ZULUZULU";
    private static final String CHANNEL = "Test";
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    private User user;
    private Fragment fragment;

    //@Before
  /*  public void init() {
        User.UserBuilder builder = new User.UserBuilder();
        builder.setSciper("123456");
        builder.setGaspar("gaspar");
        builder.setEmail("test@epfl.ch");
        builder.setFirst_names("james");
        builder.setLast_names("bond");
        user = builder.buildAuthenticatedUser();

        fragment = ChatFragment.newInstance(user, 1);
        mActivityRule.getActivity().openFragment(fragment);
    }*/

    // FROM STACKOVERFLOW, TO REMOVE OR EDIT
    public static ViewAction waitId(final Matcher<View> matcher, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return null;
            }


            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;

                if(!timeout_loup(uiController, matcher, view, endTime))
                {
                    // timeout happens
                    throw new PerformException.Builder()
                            .withCause(new TimeoutException())
                            .build();
                }

            }
        };
    }

    private static boolean timeout_loup(final UiController uiController, final Matcher<View> matcher, final View view, final long endTime){
        do {
            for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                // found view with required ID
                if (matcher.matches(child)) {
                    return true;
                }
            }

            uiController.loopMainThreadForAtLeast(50);
        }
        while (System.currentTimeMillis() < endTime);

        return false;
    }

    @Before
    public void setUp() {
        Utility.fullLogin();
    }

    @Test
    public void testUserCanSendAMessageAndReadIt() throws InterruptedException {
        /* Todo
        I don't understand how to test this without the dependence of firestore
        I don't want to write a message in the database during the test
        How can I mock the behavior of firestore..?
         */


        // DAHN UI ATTEMPT :
        Utility.openMenu();
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_chat));

        // sleep during db time
        TimeUnit.SECONDS.sleep(10);

        // Open Test channel
        onView(isRoot()).perform(waitId(withText(CHANNEL), TimeUnit.SECONDS.toMillis(20)));

        onView(withText(CHANNEL)).perform(click());


        // Type message
        onView(withId(R.id.chat_message_edit)).perform(replaceText(MSG));

        onView(withId(R.id.chat_send_button)).perform(click());
    }
}
