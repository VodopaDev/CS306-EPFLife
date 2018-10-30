package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Fragments.AboutZuluzuluFragment;
import ch.epfl.sweng.zuluzulu.Fragments.AssociationFragment;
import ch.epfl.sweng.zuluzulu.Fragments.ChannelFragment;
import ch.epfl.sweng.zuluzulu.Fragments.EventFragment;
import ch.epfl.sweng.zuluzulu.Fragments.LoginFragment;
import ch.epfl.sweng.zuluzulu.Fragments.MainFragment;
import ch.epfl.sweng.zuluzulu.Fragments.ProfileFragment;
import ch.epfl.sweng.zuluzulu.Fragments.SettingsFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_ABOUT_US_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_ASSOCIATION_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_CHANNEL_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_EVENT_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_LOGIN_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_MAIN_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_PROFILE_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_SETTINGS_FRAGMENT;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
  This class test the MainActivity as a Guest User
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityAsGuestTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    private MainActivity mActivity;

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
    }

    @Test
    public void isAuthenticated() {
        assertFalse(mActivity.isAuthenticated());
    }

    @Test
    public void getUser() {
        assertNotNull(mActivity.getUser());
    }

    //TODO: AssociationDetailFragment, EventDetailFragment
    @Test
    public void onFragmentInteractionTest(){
        // Needed to avoid original thread error
        mActivity.onFragmentInteraction(OPEN_ABOUT_US_FRAGMENT,null);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.onFragmentInteraction(OPEN_LOGIN_FRAGMENT, null);
                assertThat(mActivity.getCurrentFragment() instanceof LoginFragment, equalTo(true));

                mActivity.onFragmentInteraction(OPEN_ASSOCIATION_FRAGMENT, null);
                assertThat(mActivity.getCurrentFragment() instanceof AssociationFragment, equalTo(true));

                mActivity.onFragmentInteraction(OPEN_ABOUT_US_FRAGMENT, null);
                assertThat(mActivity.getCurrentFragment() instanceof AboutZuluzuluFragment, equalTo(true));

                mActivity.onFragmentInteraction(OPEN_MAIN_FRAGMENT, null);
                assertThat(mActivity.getCurrentFragment() instanceof MainFragment, equalTo(true));

                mActivity.onFragmentInteraction(OPEN_EVENT_FRAGMENT, null);
                assertThat(mActivity.getCurrentFragment() instanceof EventFragment, equalTo(true));

                mActivity.onFragmentInteraction(OPEN_SETTINGS_FRAGMENT, null);
                assertThat(mActivity.getCurrentFragment() instanceof SettingsFragment, equalTo(true));
            }
        });
    }

    @Test
    public void pressBackTest(){
        // Needed to avoid original thread error
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.onFragmentInteraction(OPEN_SETTINGS_FRAGMENT, null);
                mActivity.onFragmentInteraction(OPEN_ABOUT_US_FRAGMENT, null);
                onView(withId(R.layout.activity_main)).perform(ViewActions.pressBack());
                assertThat(mActivity.getCurrentFragment() instanceof SettingsFragment, equalTo(true));
            }
        });
    }
}