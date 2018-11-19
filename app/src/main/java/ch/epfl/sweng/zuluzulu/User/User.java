package ch.epfl.sweng.zuluzulu.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This parent class represent an user
 */
abstract public class User implements Serializable {

    /**
     * This list will contain the roles of the User
     */
    final protected ArrayList<UserRole> roles;

    protected User() {
        this.roles = new ArrayList<>();
    }

    public String getFirstNames() {
        return null;
    }

    public String getLastNames() {
        return null;
    }

    public String getEmail() {
        return null;
    }

    public String getSection() {
        return null;
    }

    public String getSemester() {
        return null;
    }

    public String getGaspar() {
        return null;
    }

    public String getSciper() {
        return null;
    }

    /**
     * Check if the user has the role
     *
     * @param role UserRole
     * @return boolean
     */
    public boolean hasRole(UserRole role) {
        return roles.contains(role);
    }

    /**
     * Add the role to the user roles list
     *
     * @param role UserRole
     */
    protected void addRole(UserRole role) {
        this.roles.add(role);
    }

    public abstract boolean isConnected();

    /**
     * This class is used to create a builder
     */
    public static final class UserBuilder {
        /**
         * This is the user ID, it is guaranteed to be unique.
         */
        private String sciper;

        /**
         * Gaspar account - it's the username // TODO was in string in Profile. See if it's logic or need to be number
         */
        private String gaspar;

        /**
         * User email
         */
        private String email;

        /**
         * User's section
         */
        private String section;

        /**
         * User's current semester
         */
        private String semester;


        /**
         * User first names (he can have few first names)
         */
        private String first_names;

        /**
         * User last names, same remark as first_names
         */
        private String last_names;

        // All followed ids of Associations, Chats and Events
        private List<Long> fav_assos;
        private List<Long> followed_chats;
        private List<Long> followed_events;


        /**
         * Create  an user builder
         */
        public UserBuilder() {
        }

        /**
         * Set the sciper // TODO cf number ?
         *
         * @param sciper User sciper number
         */
        public void setSciper(String sciper) {
            this.sciper = sciper;
        }

        /**
         * User gaspar - username
         *
         * @param gaspar gaspar
         */
        public void setGaspar(String gaspar) {
            this.gaspar = gaspar;
        }

        /**
         * User email
         *
         * @param email email
         */
        public void setEmail(String email) {
            if (email.contains("@")) {
                this.email = email;
            }
        }

        /**
         * User's section
         *
         * @param section section
         */
        public void setSection(String section) {
            this.section = section;
        }

        /**
         * User's current semester
         *
         * @param semester semester
         */
        public void setSemester(String semester) {
            this.semester = semester;
        }

        /**
         * User last names
         *
         * @param last_names last names
         */
        public void setLast_names(String last_names) {
            this.last_names = last_names;
        }

        /**
         * User last names
         *
         * @param first_names last names
         */
        public void setFirst_names(String first_names) {
            this.first_names = first_names;
        }

        public void setFavAssos(List<Long> fav_assos) {
            this.fav_assos = fav_assos;
        }

        public void setFollowedChats(List<Long> followed_chats) {
            this.followed_chats = followed_chats;
        }

        public void setFollowedEvents(List<Long> followed_events) {
            this.followed_events = followed_events;
        }

        /**
         * This function create a User and return the built child
         *
         * @return User Return a child of User
         */
        public User build() {
            User user = buildAuthenticatedUser();
            if (user != null) {
                return user;
            }

            return new Guest();
        }

        /**
         * Build an AuthenticatedUser
         *
         * @return AuthenticatedUser or null
         */
        public AuthenticatedUser buildAuthenticatedUser() {
            if (hasRequirementsForAuthentication())
                return new AuthenticatedUser(this.sciper, this.gaspar, this.email, this.section, this.semester, this.first_names, this.last_names, this.fav_assos, this.followed_events, this.followed_chats);
            return null;
        }

        /**
         * Build an Admin
         *
         * @return Admin or null
         */
        public Admin buildAdmin() {
            if (hasRequirementsForAuthentication()) {
                return new Admin(sciper, gaspar, email, section, semester, first_names, last_names, fav_assos, followed_events, followed_chats);
            }

            return null;
        }

        /**
         * Build guest user
         *
         * @return Guest
         */
        public Guest buildGuestUser() {
            return new Guest();
        }

        /**
         * Check the requirements for authentication
         *
         * @return boolean
         */
        private boolean hasRequirementsForAuthentication() {
            return this.sciper != null
                    && this.email != null
                    && this.section != null
                    && this.semester != null
                    && this.gaspar != null
                    && this.first_names != null
                    && this.last_names != null
                    && this.fav_assos != null
                    && this.followed_chats != null
                    && this.followed_events != null;
        }

    }
}
