package ch.epfl.sweng.zuluzulu.Structure;

public final class Admin extends AuthenticatedUser {
    protected Admin(String sciper, String gaspar, String email, String first_names, String last_names) {
        super(sciper, gaspar, email, first_names, last_names);
        addRole(UserRole.ADMIN);
    }
}
