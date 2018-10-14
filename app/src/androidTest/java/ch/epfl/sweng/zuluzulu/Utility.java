package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.view.Gravity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Use this class for functions that are used in multiple tests
 */
public class Utility {

    /**
     * Enter the username and password for login
     * To use on the LoginFragment only !
     */
    public static void login() {
        onView(withId(R.id.username)).perform(typeText("user")).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("password")).perform(closeSoftKeyboard());
        onView(withId(R.id.sign_in_button)).perform(click());
    }

    /**
     * Login from anywhere in the app
     */
    public static void fullLogin() {
        openMenu();
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_login));
        login();
    }

    /**
     * Open the menu from the mainActivity
     */
    public static void openMenu() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
    }

    public static void checkFragmentIsOpen(int id){
        onView(withId(id)).check(matches(isDisplayed()));
    }


}
