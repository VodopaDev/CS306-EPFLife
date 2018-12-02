package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.TestingUtility.MockedProxy;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AssociationDetailFragmentTest extends TestWithAuthenticatedAndFragment<AssociationFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());

        fragment = AssociationFragment.newInstance(user);
    }

    @Test
    public void notFollowedAssociationTest() {
        onView(withText("Favorites")).perform(ViewActions.click());
        onView(withText("ForumEPFL")).check(doesNotExist());
    }
}
