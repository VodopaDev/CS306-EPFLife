package ch.epfl.sweng.zuluzulu.TestingUtility;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.IdlingResource.IdlingResourceFactory;
import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.User.User;
import ch.epfl.sweng.zuluzulu.Utility;

@RunWith(AndroidJUnit4.class)
public abstract class TestWithUserAndFragment<U extends User, F extends SuperFragment> {
    protected U user;
    protected F fragment;


    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    @Before
    public void init() {
        // Add the user
        initUser();
        Utility.addUserToMainIntent(mActivityRule, user);
        // Register the idling resource
        IdlingRegistry.getInstance().register(IdlingResourceFactory.getCountingIdlingResource());
        // Open the fragment
        initFragment();
        assert (fragment != null);
        mActivityRule.getActivity().openFragment(fragment);
    }

    public MainActivity getMainActivity() {
        return mActivityRule.getActivity();
    }

    public abstract void initFragment();

    public abstract void initUser();
}
