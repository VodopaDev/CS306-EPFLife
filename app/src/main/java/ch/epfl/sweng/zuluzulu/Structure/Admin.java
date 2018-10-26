package ch.epfl.sweng.zuluzulu.Structure;

import java.util.List;

/**
 * This represent the Admin class
 */
public final class Admin extends AuthenticatedUser {

    protected Admin(String sciper, String gaspar, String email, String section, String first_names, String last_names, List<Integer> fav_assos, List<Integer> followed_events, List<Integer> followed_chats) {
        super(sciper, gaspar, email, section, first_names, last_names, fav_assos, followed_events, followed_chats);

        // Admin has role ADMIN
        addRole(UserRole.ADMIN);
    }
}
