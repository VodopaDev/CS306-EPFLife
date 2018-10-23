package ch.epfl.sweng.zuluzulu;

import java.util.ArrayList;
import java.util.Arrays;

import ch.epfl.sweng.zuluzulu.Structure.User;

public class Utility {

    /**
     * Create a user for the tests
     *
     * @return Return a user
     */
    public static User createTestUser() {
        User.UserBuilder builder = new User.UserBuilder();
        builder.setSciper("123456");
        builder.setGaspar("gaspar");
        builder.setEmail("test@epfl.ch");
        builder.setFirst_names("James");
        builder.setLast_names("Bond");
        builder.setFavAssos(Arrays.asList(1,2));
        builder.setFollowedEvents(new ArrayList<Integer>());
        builder.setFollowedChats(new ArrayList<Integer>());

        User user = builder.buildAuthenticatedUser();
        assert (user != null);
        assert (user.isConnected());

        return user;
    }

}
