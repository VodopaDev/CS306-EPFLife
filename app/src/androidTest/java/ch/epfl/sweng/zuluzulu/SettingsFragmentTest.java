package ch.epfl.sweng.zuluzulu;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.Fragments.SettingsFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.zuluzulu.Utility.openMenu;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SettingsFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
        openMenu();
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_settings));
    }

    @Test
    public void clickOnNightLightSwitchBis() {
        onView(withId(R.id.switch_night_light))
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

    @Test
    public void testClickOnAnonym() throws InterruptedException {
        SharedPreferences preferences = mActivityTestRule.getActivity().getPreferences(Context.MODE_PRIVATE);
        boolean anonym = preferences.getBoolean(SettingsFragment.PREF_KEY_ANONYM, false);


        onView(withId(R.id.switch_notifications))
                .perform(click());

        onView(withId(R.id.switch_chat_anonym)).perform(click());
        TimeUnit.SECONDS.sleep(1);
        boolean anonymAfterClick = preferences.getBoolean(SettingsFragment.PREF_KEY_ANONYM, false);
        assertNotEquals(anonymAfterClick, anonym);

        onView(withId(R.id.switch_chat_anonym)).perform(click());
        boolean anonymAfterTwoClick = preferences.getBoolean(SettingsFragment.PREF_KEY_ANONYM, false);
        assertEquals(anonymAfterTwoClick, anonym);
    }

}
