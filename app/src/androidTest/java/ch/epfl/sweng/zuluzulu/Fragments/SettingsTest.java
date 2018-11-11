package ch.epfl.sweng.zuluzulu.Fragments;


import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Fragments.SettingsFragment;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithGuestAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.zuluzulu.Utility.openMenu;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SettingsTest extends TestWithGuestAndFragment<SettingsFragment> {

    @Override
    public void initFragment() {
        fragment = SettingsFragment.newInstance();
    }

    @Test
    public void clickOnNightLightSwitchBis() {
        onView(ViewMatchers.withId(R.id.switch_night_light))
                .perform(click());

    }

    @Test
    public void clickOnNotificationSwitchTest() {
        onView(withId(R.id.switch_notifications))
                .perform(click());
    }

    @Test
    public void clickOnClearButtonTest() {
        onView(withId(R.id.button_clear_cache))
                .perform(click());
    }


}
