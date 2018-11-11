package ch.epfl.sweng.zuluzulu.TestingUtility;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;

import ch.epfl.sweng.zuluzulu.Fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.Structure.User;
import ch.epfl.sweng.zuluzulu.Utility;

public abstract class TestWithUserAndFragment<U extends User, F extends SuperFragment> {
    protected U user;
    protected F fragment;


    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    @Before
    public void init(){
        initUser();
        initFragment();

        // Add the user
        Utility.addUserToMainIntent(mActivityRule, user);
        // Register the idling resource
        IdlingRegistry.getInstance().register(mActivityRule.getActivity().getCountingIdlingResource());
        // Open the fragment
        mActivityRule.getActivity().openFragment(fragment);
    }

    public MainActivity getMainActivity(){
        return mActivityRule.getActivity();
    }

    public abstract void initFragment();

    public abstract void initUser();
}
