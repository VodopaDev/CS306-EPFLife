package ch.epfl.sweng.zuluzulu.TestingUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;

public class Utility {

    private static final String sciper = "123456";
    private static final String gaspar = "gaspar";
    private static final String email = "test@epfl.ch";
    private static final String section = "IN";
    private static final String semester = "BA5";
    private static final String firstName = "James";
    private static final String lastName = "Bond";
    private static final List<String> favAssos = Arrays.asList("0", "1");
    private static final List<String> followedEvents = Collections.singletonList("1");
    private static final List<String> followedChats = new ArrayList<>();

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
        builder.setFollowedAssociations(favAssos);
        builder.setFollowedEvents(followedEvents);
        builder.setFollowedChannels(followedChats);

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

    /**
     * Return a default channel
     *
     * @return a default channel
     */
    public static Channel defaultChannel() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", 1L);
        data.put("name", "name");
        data.put("description", "description");
        data.put("restrictions", new HashMap<>());
        FirebaseMapDecorator fmap = new FirebaseMapDecorator(data);
        return new Channel(fmap);
    }
}
