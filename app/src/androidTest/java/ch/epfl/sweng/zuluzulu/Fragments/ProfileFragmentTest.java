package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.User;
import ch.epfl.sweng.zuluzulu.Utility;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class ProfileFragmentTest {
    private User user;
    private Fragment fragment;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        user = Utility.createTestUser();

        fragment = ProfileFragment.newInstance(user);
        mActivityRule.getActivity().openFragment(fragment);
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