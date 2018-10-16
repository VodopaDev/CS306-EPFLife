package ch.epfl.sweng.zuluzulu.Fragments;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;

import org.junit.Before;
import org.junit.Rule;

import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.Structure.User;
import ch.epfl.sweng.zuluzulu.Utility;

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
        mActivityRule.getActivity().openFragment(fragment);    }
}