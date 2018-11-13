package ch.epfl.sweng.zuluzulu;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Fragments.AboutZuluzuluFragment;
import ch.epfl.sweng.zuluzulu.Fragments.AssociationDetailFragment;
import ch.epfl.sweng.zuluzulu.Fragments.AssociationFragment;
import ch.epfl.sweng.zuluzulu.Fragments.ChannelFragment;
import ch.epfl.sweng.zuluzulu.Fragments.ChatFragment;
import ch.epfl.sweng.zuluzulu.Fragments.EventFragment;
import ch.epfl.sweng.zuluzulu.Fragments.LoginFragment;
import ch.epfl.sweng.zuluzulu.Fragments.MainFragment;
import ch.epfl.sweng.zuluzulu.Fragments.PostFragment;
import ch.epfl.sweng.zuluzulu.Fragments.ProfileFragment;
import ch.epfl.sweng.zuluzulu.Fragments.SettingsFragment;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAdminAndFragment;

import static ch.epfl.sweng.zuluzulu.CommunicationTag.INCREMENT_IDLING_RESOURCE;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPENING_WEBVIEW;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_ABOUT_US_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_ASSOCIATION_DETAIL_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_ASSOCIATION_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_CHANNEL_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_CHAT_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_EVENT_DETAIL_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_EVENT_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_LOGIN_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_MAIN_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_POST_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_PROFILE_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_SETTINGS_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.SET_TITLE;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.SET_USER;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.JMock1Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class MainActivityListenerTest extends TestWithAdminAndFragment<MainFragment> {

    @Override
    public void initFragment() {
        fragment = MainFragment.newInstance(user);
    }

    @Test
    public void communicationTest() throws Throwable {
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Utils to modify the main activity
                getMainActivity().onFragmentInteraction(SET_TITLE, "new_title");
                assertEquals("new_title", getMainActivity().getTitle());
                //SET_USER,
                //OPENING_WEBVIEW,

                getMainActivity().onFragmentInteraction(OPEN_MAIN_FRAGMENT, user);
                assertTrue(getMainActivity().getCurrentFragment() instanceof MainFragment);

                getMainActivity().onFragmentInteraction(OPEN_SETTINGS_FRAGMENT, user);
                assertTrue(getMainActivity().getCurrentFragment() instanceof SettingsFragment);

                getMainActivity().onFragmentInteraction(OPEN_ABOUT_US_FRAGMENT, user);
                assertTrue(getMainActivity().getCurrentFragment() instanceof AboutZuluzuluFragment);

                getMainActivity().onFragmentInteraction(OPEN_PROFILE_FRAGMENT, user);
                assertTrue(getMainActivity().getCurrentFragment() instanceof ProfileFragment);

                getMainActivity().onFragmentInteraction(OPEN_CHAT_FRAGMENT, Utility.defaultChannel());
                assertTrue(getMainActivity().getCurrentFragment() instanceof ChatFragment);

                getMainActivity().onFragmentInteraction(OPEN_POST_FRAGMENT, Utility.defaultChannel());
                assertTrue(getMainActivity().getCurrentFragment() instanceof PostFragment);

                getMainActivity().onFragmentInteraction(OPEN_CHANNEL_FRAGMENT, user);
                assertTrue(getMainActivity().getCurrentFragment() instanceof ChannelFragment);

                getMainActivity().onFragmentInteraction(OPEN_EVENT_FRAGMENT, user);
                assertTrue(getMainActivity().getCurrentFragment() instanceof EventFragment);

                //getMainActivity().onFragmentInteraction(OPEN_EVENT_DETAIL_FRAGMENT, Utility.defaultEvent());
                //assertTrue(getMainActivity().getCurrentFragment() instanceof EventDetailFragment);

                getMainActivity().onFragmentInteraction(OPEN_ASSOCIATION_FRAGMENT, user);
                assertTrue(getMainActivity().getCurrentFragment() instanceof AssociationFragment);

                getMainActivity().onFragmentInteraction(OPEN_ASSOCIATION_DETAIL_FRAGMENT, Utility.defaultAssociation());
                assertTrue(getMainActivity().getCurrentFragment() instanceof AssociationDetailFragment);
            }
        });

    }
}
