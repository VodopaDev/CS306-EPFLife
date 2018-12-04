package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Database.MockedProxy;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.Fragments.AdminFragments.ChangeUserRoleFragment;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAdminAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ChangeUserRoleFragmentAsAdminTest extends TestWithAdminAndFragment {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());
        fragment = ChangeUserRoleFragment.newInstance();
    }

    @Test
    public void filteringIsPossible(){
        onView(withId(R.id.user_role_list)).check(matches(hasChildCount(1)));
        onView(withId(R.id.user_role_searchbar)).perform(ViewActions.typeText("1265"));
        onView(withId(R.id.user_role_list)).check(matches(hasChildCount(0)));
        onView(withId(R.id.user_role_searchbar)).perform(ViewActions.clearText()).perform(ViewActions.typeText("1"));
        onView(withId(R.id.user_role_list)).check(matches(hasChildCount(1)));
    }
}
