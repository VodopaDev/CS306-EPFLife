package ch.epfl.sweng.zuluzulu;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.Fragments.ProfileFragment;
import ch.epfl.sweng.zuluzulu.Structure.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;

public class ProfileFragmentTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);
    private User user;
    private Fragment fragment;

    @Before
    public void init() {
        user = Utility.createTestUser();
        Utility.addUserToMainIntent(mActivityRule, user);

        fragment = ProfileFragment.newInstance(user);
        mActivityRule.getActivity().openFragment(fragment);
    }

    @Test
    public void checkName() {
        onView(withId(R.id.profile_gaspar_text)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void checkGaspar() {
        onView(withId(R.id.profile_gaspar_text)).check(matches(withText(containsString(user.getGaspar()))));
    }

    @Test
    public void checkEmail() {
        onView(withId(R.id.profile_email_edit)).check(matches(withText(containsString(user.getEmail()))));
    }

    @Test
    public void checkSciper() {
        onView(withId(R.id.profile_sciper_edit)).check(matches(withText(containsString(user.getSciper()))));
    }
}