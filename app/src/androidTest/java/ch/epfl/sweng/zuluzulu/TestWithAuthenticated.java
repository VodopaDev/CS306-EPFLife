package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;

import ch.epfl.sweng.zuluzulu.Fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.Structure.User;

/**
 * This class allow us to create the main activity as if we were connected.
 * It creates an user and allow child to use this User.
 *
 * Author @dahn
 */
public abstract class TestWithAuthenticated {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);


    private User user;

    @Before
    public void setUpLogin() {
        this.user = Utility.createTestUser();
        Utility.addUserToMainIntent(mActivityRule, this.user);
        IdlingRegistry.getInstance().register(getMainActivity().getCountingIdlingResource());
    }

    /**
     * Return the user for the childs
     * @return User
     */
    protected User getUser() {
        return this.user;
    }

    protected CountingIdlingResource getCountingIdlingRessource(){
        return mActivityRule.getActivity().getCountingIdlingResource();
    }

    protected MainActivity getMainActivity(){
        return mActivityRule.getActivity();
    }

    protected void openFragment(SuperFragment fragment){
        getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getMainActivity().openFragment(fragment);
            }
        });
    }

    protected SuperFragment getCurrentFragment(){
        return mActivityRule.getActivity().getCurrentFragment();
    }

}
