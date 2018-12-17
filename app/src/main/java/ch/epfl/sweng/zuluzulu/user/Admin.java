package ch.epfl.sweng.zuluzulu.user;

import java.util.List;

/**
 * This represent the Admin class
 */
public final class Admin extends AuthenticatedUser {

    protected Admin(String sciper, String gaspar, String email, String section, String semester, String firstNames, String lastNames,
                    List<String> followedAssociations, List<String> followedEvents, List<String> followedChannels) {

        super(sciper, gaspar, email, section, semester, firstNames,
                lastNames, followedAssociations, followedEvents, followedChannels);

        // Admin has role ADMIN
        addRole(UserRole.ADMIN);
    }
}
