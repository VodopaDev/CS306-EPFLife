package ch.epfl.sweng.zuluzulu.Structure;

import java.io.Serializable;
import java.util.ArrayList;

abstract public class User implements Serializable {

    /**
     * This list will contain the roles of the User
     */
    final protected ArrayList<UserRole> roles;

    protected User() {
        this.roles = new ArrayList<UserRole>();
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

    public String getGaspar() {
        return null;
    }

    public String getSciper() {
        return null;
    }

    /**
     * Check if the user has the role
     * @param role UserRole
     * @return boolean
     */
    public boolean hasRole(UserRole role){
        return roles.contains(role);
    }

    /**
     * Add the role to the user roles list
     * @param role UserRole
     */
    protected void addRole(UserRole role){
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
         * User first names (he can have few first names)
         */
        private String first_names;

        /**
         * User last names, same remark as first_names
         */
        private String last_names;

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
            if (hasRequirementsForAuthentication()) {
                return new AuthenticatedUser(this.sciper, this.gaspar, this.email, this.first_names, this.last_names);
            }

            return null;
        }

        /**
         * Build an Admin
         *
         * @return Admin or null
         */
        public Admin buildAdmin() {
            if (hasRequirementsForAuthentication()) {
                return new Admin(this.sciper, this.gaspar, this.email, this.first_names, this.last_names);
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
         * @return boolean
         */
        private boolean hasRequirementsForAuthentication(){
            return  this.sciper != null
                    && this.email != null
                    && this.gaspar != null
                    && this.first_names != null
                    && this.last_names != null;
        }

    }
}
