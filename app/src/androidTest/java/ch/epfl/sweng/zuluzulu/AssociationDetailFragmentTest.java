package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.Fragments.AssociationFragment;
import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AssociationDetailFragmentTest extends TestWithLogin{

    @Before
    public void initAuthenticatedTest() {
        IdlingRegistry.getInstance().register(mActivityRule.getActivity().getCountingIdlingResource());
        AssociationFragment fragment = AssociationFragment.newInstance(getUser());
        mActivityRule.getActivity().openFragment(fragment);
    }

    @Test
    public void thereIsSomething() {
        onView(withText("Agepoly")).perform(ViewActions.click());
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void thereIsNothing() {
        onView(withText("ForumEPFL")).perform(ViewActions.click());
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
