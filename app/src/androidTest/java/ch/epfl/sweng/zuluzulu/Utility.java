package ch.epfl.sweng.zuluzulu;

import android.content.Intent;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.view.Gravity;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.ChatMessage;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.Structure.EventBuilder;
import ch.epfl.sweng.zuluzulu.Structure.EventDate;
import ch.epfl.sweng.zuluzulu.Structure.Post;
import ch.epfl.sweng.zuluzulu.User.Admin;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.Guest;
import ch.epfl.sweng.zuluzulu.User.User;
import ch.epfl.sweng.zuluzulu.User.UserRole;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Use this class for functions that are used in multiple tests
 */
public class Utility {

    /**
     * Create a guest user
     * @return Return a Guest
     */
    public static Guest createTestGuest(){
        return (new User.UserBuilder()).buildGuestUser();
    }

    /**
     * Create a user for the tests
     *
     * @return Return an AithenticatedUser
     */
    public static AuthenticatedUser createTestAuthenticated() {
        User.UserBuilder builder = createFilledUserBuilder();
        User user = builder.buildAuthenticatedUser();
        assert (user != null);
        assert (user.isConnected());

        return (AuthenticatedUser)user;
    }

    /**
     * Create a admin for the tests
     *
     * @return Return an Admin
     */
    public static Admin createTestAdmin() {
        User.UserBuilder builder = createFilledUserBuilder();
        User user = builder.buildAdmin();
        assert (user != null);
        assert (user.hasRole(UserRole.ADMIN));

        return (Admin)user;
    }

    /**
     * Add user to main
     * <p>
     * !!! TO READ !!!
     *
     * @param mActivityRule Activity rule
     * @param user          User
     * @warning NEED TO BE CALLED TO CREATE THE ACTIVITY
     * USE IN RULE : new ActivityTestRule<>(MainActivity.class, false, false);
     * <p>
     * It's allow us to not start the Activity before !
     * <p>
     * !!! TO READ !!!
     */
    public static void addUserToMainIntent(ActivityTestRule<MainActivity> mActivityRule, User user) {
        // Put the user into the main
        Intent i = new Intent();
        i.putExtra("user", user);
        mActivityRule.launchActivity(i);
    }

    /**
     * Open the menu from the mainActivity
     */
    public static void openMenu() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
    }

    /**
     * Close the menu from the mainActivity
     */
    public static void closeMenu() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isOpen(Gravity.LEFT)))
                .perform(DrawerActions.close());
    }

    /**
     * Check if the fragment is open
     *
     * @param id fragment id
     */
    public static void checkFragmentIsOpen(int id) {
        onView(withId(id)).check(matches(isDisplayed()));
    }

    /**
     * Check if the fragment is not open
     *
     * @param id fragment id
     */
    public static void checkFragmentIsClosed(int id) {
        onView(withId(id)).check(doesNotExist());
    }

    /**
     * Return a userbuilder already filled with all the user informations
     *
     * @return UserBuilder
     */
    private static User.UserBuilder createFilledUserBuilder() {
        User.UserBuilder builder = new User.UserBuilder();
        builder.setSciper("123456");
        builder.setGaspar("gaspar");
        builder.setEmail("test@epfl.ch");
        builder.setSection("IN");
        builder.setSemester("BA5");
        builder.setFirst_names("James");
        builder.setLast_names("Bond");
        builder.setFollowedAssociations(Collections.singletonList("1"));
        builder.setFollowedEvents(Arrays.asList("1", "2", "3"));
        builder.setFollowedChannels(Collections.singletonList("1"));

        return builder;
    }

    /**
     * Return a default channel
     *
     * @return a default channel
     */
    public static Channel defaultChannel() {
        return new Channel(
                "1",
                "Testing channel",
                "The name is self explaining",
                Collections.EMPTY_MAP,
                null
        );
    }

    public static Post defaultPost() {
        return new Post(
                "1",
                "1",
                null,
                "mon message ne sert à rien",
                "Nico",
                "270103",
                Timestamp.now().toDate(),
                "#F0E68C",
                0,
                0,
                Collections.EMPTY_LIST,
                Collections.EMPTY_LIST
        );
    }

    public static Association defaultAssociation(){
        return new Association(
                "1",
                "Agepoly",
                "Small description",
                "A bit longer description without a lot of details",
                null,
                null,
                Collections.singletonList("1"),
                "1"
        );
    }

    public static Event defaultEvent(){
        return new EventBuilder()
                .setId("1")
                .setName("Fiesta time")
                .setChannelId("1")
                .setAssosId("1")
                .setShortDesc("Is this a real event?")
                .setLongDesc("Of course not, you should check this beautiful description")
                .setDate(new EventDate(new Date(10000000), new Date(10500000)))
                .setFollowers(new ArrayList<>())
                .setOrganizer("I'm the organizer")
                .setPlace("Not at EPFL")
                .setIconUri(null)
                .setBannerUri(null)
                .setUrlPlaceAndRoom("myplace")
                .setWebsite("www.fakefiesta.com")
                .setContact("Nico")
                .setCategory("no category")
                .setSpeaker("Nico")
                .build();
    }

    public static ChatMessage defaultMessage() {
        return new ChatMessage("1", "1", "message", new Date(), "sender", "111111");
    }
}
