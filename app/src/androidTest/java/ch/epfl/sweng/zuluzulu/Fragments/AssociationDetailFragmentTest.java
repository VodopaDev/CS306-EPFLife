package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAuthenticatedAndFragment;
import ch.epfl.sweng.zuluzulu.Utility;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class AssociationDetailFragmentTest extends TestWithAuthenticatedAndFragment<AssociationDetailFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());
        fragment = AssociationDetailFragment.newInstance(user, Utility.defaultAssociation());
    }

    @Test
    public void notFollowedAssociationTest() {
        onView(withId(R.id.association_detail_fav)).perform(ViewActions.click());
        onView(withId(R.id.association_detail_fav)).perform(ViewActions.click());
    }

    @Test
    public void tchatTest(){
        onView(withId(R.id.association_detail_chat_name)).perform(ViewActions.click());
    }
}
