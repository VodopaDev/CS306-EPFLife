package ch.epfl.sweng.zuluzulu;

import android.content.Intent;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.view.Gravity;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
     *
     * @return Return a Guest
     */
    public static Guest createTestGuest() {
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

        return (AuthenticatedUser) user;
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

        return (Admin) user;
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
    public static User.UserBuilder createFilledUserBuilder() {
        User.UserBuilder builder = new User.UserBuilder();
        builder.setSciper("0");
        builder.setGaspar("gaspar");
        builder.setEmail("test@epfl.ch");
        builder.setSection("IN");
        builder.setSemester("BA5");
        builder.setFirst_names("James");
        builder.setLast_names("Bond");
        builder.setFollowedAssociations(Collections.singletonList("0"));
        builder.setFollowedEvents(Collections.singletonList("0"));
        builder.setFollowedChannels(Collections.singletonList("0"));

        return builder;
    }

    /**
     * Return a default channel
     *
     * @return a default channel
     */
    public static Channel defaultChannel() {
        return new Channel(
                "0",
                "Testing channel",
                "The name is self explaining",
                new HashMap<>(),
                null
        );
    }

    public static Post defaultPost0() {
        return new Post(
                "0",
                "0",
                null,
                "mon message ne sert à rien",
                "Nico",
                "1",
                Timestamp.now().toDate(),
                "#F0E68C",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }
    public static Post defaultPost1() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return new Post(
                "1",
                "0",
                null,
                "mon message ne sert à rien",
                "Nico",
                "1",
                cal.getTime(),
                "#F0E68C",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    public static Association defaultAssociation() {
        return new Association(
                "0",
                "Agepoly",
                "Small description",
                "A bit longer description without a lot of details",
                null,
                null,
                Collections.singletonList("0"),
                "0"
        );
    }

    public static Event defaultEvent() {
        return new EventBuilder()
                .setId("0")
                .setName("Fiesta time")
                .setChannelId("0")
                .setAssosId("0")
                .setShortDesc("Is this a real event?")
                .setLongDesc("Of course not, you should check this beautiful description")
                .setDate(new EventDate(new Date(1514764800000L), new Date(1517443200000L)))
                .setFollowers(Collections.singletonList("0"))
                .setOrganizer("I'm the organizer")
                .setPlace("Not at EPFL")
                .setIconUri(null)
                .setBannerUri(null)
                .setUrlPlaceAndRoom("myplace")
                .setWebsite("https://www.epfl.ch")
                .setContact("Nico")
                .setCategory("no category")
                .setSpeaker("Nico")
                .build();
    }

//    public static Event currentTimeEvent() {
//        return new EventBuilder()
//                .setId("0")
//                .setName("Current time")
//                .setChannelId("0")
//                .setAssosId("0")
//                .setShortDesc("Is this a real event?")
//                .setLongDesc("Of course not, you should check this beautiful description")
//                .setDate(new EventDate(new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())))
//                .setFollowers(Collections.singletonList("0"))
//                .setOrganizer("I'm the organizer")
//                .setPlace("Not at EPFL")
//                .setIconUri(null)
//                .setBannerUri(null)
//                .setUrlPlaceAndRoom("myplace")
//                .setWebsite("https://www.epfl.ch")
//                .setContact("Nico")
//                .setCategory("no category")
//                .setSpeaker("Nico")
//                .build();
//    }

    public static ChatMessage defaultMessage0() {
        return new ChatMessage("0", "0", "message?", new Date(2000), "auth", "0");
    }

    public static ChatMessage defaultMessage1() {
        return new ChatMessage("1", "0", "message!", new Date(20000), "admin", "1");
    }

    public static Map<String, Object> createMapWithAll() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", "");
        map.put("name", "");
        map.put("short_description", "");
        map.put("long_description", "");
        map.put("category", "");
        map.put("icon_uri", "");
        map.put("banner_uri", "");
        map.put("followers", "");
        map.put("channel_id", "");
        map.put("association_id", "");
        map.put("start_date", "");
        map.put("end_date", "");
        map.put("place", "");
        map.put("organizer", "");
        map.put("url_place_and_room", "");
        map.put("website", "");
        map.put("contact", "");
        map.put("speaker", "");
        map.put("followed_associations", new ArrayList<String>());
        map.put("followed_events", new ArrayList<String>());
        map.put("followed_channels", new ArrayList<String>());
        map.put("roles", Arrays.asList("USER"));
        map.put("first_name", "");
        map.put("last_name", "");
        map.put("section", "");
        map.put("semester", "");
        map.put("gaspar", "");
        map.put("email", "");
        map.put("sciper", "");
        map.put("sender_name", "");
        map.put("sender_sciper", "");
        map.put("message", "");
        map.put("time", new Date());
        map.put("restrictions", "");
        map.put("color", "");
        map.put("nb_ups", 1L);
        map.put("nb_responses", 1L);
        map.put("up_scipers", new ArrayList<String>());
        map.put("down_scipers", new ArrayList<String>());

        return map;
    }
}
