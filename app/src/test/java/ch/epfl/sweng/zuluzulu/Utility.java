package ch.epfl.sweng.zuluzulu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.User;

public class Utility {

    private static final String sciper = "123456";
    private static final String gaspar = "gaspar";
    private static final String email = "test@epfl.ch";
    private static final String section = "IN";
    private static final String semester = "BA5";
    private static final String firstName = "James";
    private static final String lastName = "Bond";
    private static final List<Integer> favAssos = Arrays.asList(1, 2);
    private static final List<Integer> followedEvents = new ArrayList<>();
    private static final List<Integer> followedChats = new ArrayList<>();

    private Utility() {
    }

    /**
     * Create a default user builder for the tests
     *
     * @return Return a default user builder
     */
    public static User.UserBuilder createTestUserBuilder() {
        User.UserBuilder builder = new User.UserBuilder();
        builder.setSciper(sciper);
        builder.setGaspar(gaspar);
        builder.setEmail(email);
        builder.setSection(section);
        builder.setSemester(semester);
        builder.setFirst_names(firstName);
        builder.setLast_names(lastName);
        builder.setFavAssos(favAssos);
        builder.setFollowedEvents(followedEvents);
        builder.setFollowedChats(followedChats);

        return builder;
    }

    /**
     * Create a default user for the tests
     *
     * @return Return a default user
     */
    public static AuthenticatedUser createTestUser() {
        AuthenticatedUser user = createTestUserBuilder().buildAuthenticatedUser();
        assert (user != null);
        assert (user.isConnected());

        return user;
    }

    /**
     * Create a custom user for the tests
     *
     * @return Return a custom user
     */
    public static AuthenticatedUser createTestCustomUser(User.UserBuilder builder) {
        AuthenticatedUser user = builder.buildAuthenticatedUser();
        assert (user != null);
        assert (user.isConnected());

        return user;
    }
}
