package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedUser;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AssociationDetailFragmentTest extends TestWithAuthenticatedUser {

    @Before
    public void initAuthenticatedTest() {
        IdlingRegistry.getInstance().register(mActivityRule.getActivity().getCountingIdlingResource());
        AssociationFragment fragment = AssociationFragment.newInstance(getUser());
        mActivityRule.getActivity().openFragment(fragment);
    }

    @Test
    public void unfollowFollowTest() {
        onView(withText("Favorites")).perform(ViewActions.click());
        onView(withText("Agepoly")).check(matches(isDisplayed()));
    }

    @Test
    public void notFollowedAssociationTest() {
        onView(withText("Favorites")).perform(ViewActions.click());
        onView(withText("ForumEPFL")).check(doesNotExist());
    }

}
