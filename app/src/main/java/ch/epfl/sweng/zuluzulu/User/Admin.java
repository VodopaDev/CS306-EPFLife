package ch.epfl.sweng.zuluzulu.User;

import java.util.List;

/**
 * This represent the Admin class
 */
public final class Admin extends AuthenticatedUser {

    protected Admin(String sciper, String gaspar, String email, String section, String semester, String first_names, String last_names, List<Long> fav_assos, List<Long> followed_events, List<Long> followed_chats) {
        super(sciper, gaspar, email, section, semester, first_names, last_names, fav_assos, followed_events, followed_chats);

        // Admin has role ADMIN
        addRole(UserRole.ADMIN);
    }
}
