package ch.epfl.sweng.zuluzulu.structure.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This parent class represent an user
 */
abstract public class User implements Serializable {

    /**
     * This list will contain the roles of the User
     */
    final Set<UserRole> roles;

    User() {
        this.roles = new TreeSet<>();
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
    public void addRole(UserRole role) {
        this.roles.add(role);
    }

    public List<String> getRoles() {
        List<String> result = new ArrayList<>();
        for (UserRole role : roles)
            result.add(role.name());
        return result;
    }

    public void setRoles(List<String> roles) {
        this.roles.clear();
        for (String role : roles)
            this.roles.add(UserRole.valueOf(role));
    }

    public abstract boolean isConnected();

    /**
     * This class is used to create a builder
     */
    public static final class UserBuilder {

        private String sciper;
        private String gaspar;
        private String email;
        private String section;
        private String semester;

        /**
         * User first names (he can have few first names)
         */
        private String first_names;
        private String last_names;

        // All followed ids of Associations, Chats and Events
        private List<String> followedAssociations;
        private List<String> followedChannels;
        private List<String> followedEvents;


        /**
         * Create  an user builder
         */
        public UserBuilder() {
        }

        /**
         * Set the sciper
         *
         * @param sciper User sciper number
         */
        public UserBuilder setSciper(String sciper) {
            this.sciper = sciper;
            return this;
        }

        /**
         * User gaspar - username
         *
         * @param gaspar gaspar
         */
        public UserBuilder setGaspar(String gaspar) {
            this.gaspar = gaspar;
            return this;
        }

        /**
         * User email
         *
         * @param email email
         */
        public UserBuilder setEmail(String email) {
            if (email.contains("@")) {
                this.email = email;
            }
            return this;
        }

        /**
         * User's section
         *
         * @param section section
         */
        public UserBuilder setSection(String section) {
            this.section = section;
            return this;
        }

        /**
         * User's current semester
         *
         * @param semester semester
         */
        public UserBuilder setSemester(String semester) {
            this.semester = semester;
            return this;
        }

        /**
         * User last names
         *
         * @param last_names last names
         */
        public UserBuilder setLast_names(String last_names) {
            this.last_names = last_names;
            return this;
        }

        /**
         * User last names
         *
         * @param first_names last names
         */
        public UserBuilder setFirst_names(String first_names) {
            this.first_names = first_names;
            return this;
        }

        public UserBuilder setFollowedAssociations(List<String> followedAssociations) {
            this.followedAssociations = new ArrayList<>(followedAssociations);
            return this;
        }

        public UserBuilder setFollowedChannels(List<String> followedChannels) {
            this.followedChannels = new ArrayList<>(followedChannels);
            return this;
        }

        public UserBuilder setFollowedEvents(List<String> followedEvents) {
            this.followedEvents = new ArrayList<>(followedEvents);
            return this;
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
                return new AuthenticatedUser(this.sciper, this.gaspar, this.email, this.section, this.semester, this.first_names, this.last_names, this.followedAssociations, this.followedEvents, this.followedChannels);
            return null;
        }

        /**
         * Build an Admin
         *
         * @return Admin or null
         */
        public Admin buildAdmin() {
            if (hasRequirementsForAuthentication()) {
                return new Admin(sciper, gaspar, email, section, semester, first_names, last_names, followedAssociations, followedEvents, followedChannels);
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
                    && this.followedAssociations != null
                    && this.followedChannels != null
                    && this.followedEvents != null;
        }
    }
}
