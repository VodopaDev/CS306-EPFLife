package ch.epfl.sweng.zuluzulu;


import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.zuluzulu.Utility.openMenu;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SettingsTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    public static void goToEvent() {
        openMenu();
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_settings));
    }

    @Test
    public void clickOnNightLightSwitchBis() {
        goToEvent();
        onView(withId(R.id.switch_night_light))
                .perform(click());

    }

    @Test
    public void clickOnNotificationSwitchTest() {
        goToEvent();
        onView(withId(R.id.switch_notifications))
                .perform(click());
    }

    @Test
    public void clickOnClearButtonTest() {
        goToEvent();
        onView(withId(R.id.button_clear_cache))
                .perform(click());
    }
}
