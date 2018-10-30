package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.NavigationViewActions;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.Fragments.AboutZuluzuluFragment;
import ch.epfl.sweng.zuluzulu.Fragments.AssociationFragment;
import ch.epfl.sweng.zuluzulu.Fragments.ChannelFragment;
import ch.epfl.sweng.zuluzulu.Fragments.EventFragment;
import ch.epfl.sweng.zuluzulu.Fragments.LoginFragment;
import ch.epfl.sweng.zuluzulu.Fragments.MainFragment;
import ch.epfl.sweng.zuluzulu.Fragments.ProfileFragment;
import ch.epfl.sweng.zuluzulu.Fragments.SettingsFragment;
import ch.epfl.sweng.zuluzulu.Structure.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
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
import static org.junit.Assert.assertTrue;

/**
 * This class test the MainActivity as a connected User
 */
public class MainActivityAsAuthenticatedTest extends TestWithAuthenticated {


    @Test
    public void isAuthenticated() {
        assertTrue(mActivityRule.getActivity().isAuthenticated());
    }

    @Test
    public void userFragmentOpen() {
        onView(withId(R.id.main_user_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void selectItemTest(){
        Utility.openMenu();
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_associations));
        onView(withId(R.id.association_fragment_all_button)).check(matches(isDisplayed()));

        Utility.openMenu();
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_events));
        onView(withId(R.id.event_fragment_all_button)).check(matches(isDisplayed()));
    }

    //TODO: ChatFragment
    @Test
    public void listenerTest(){
        // Needed to avoid original thread error
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getMainActivity().onFragmentInteraction(OPEN_CHANNEL_FRAGMENT, null);
                assertThat(getCurrentFragment() instanceof ChannelFragment, equalTo(true));

                getMainActivity().onFragmentInteraction(OPEN_PROFILE_FRAGMENT, null);
                assertThat(getCurrentFragment() instanceof ProfileFragment, equalTo(true));
            }
        });

    }
}