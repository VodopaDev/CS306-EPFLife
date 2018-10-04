package ch.epfl.sweng.zuluzulu.Structure;

import java.util.List;

abstract public class User {

    /* DEFINE ALL THE USER FUNCTION*/

    public String getFirst_names() {
        return null;
    }

    public String getLast_names() {
        return null;
    }

    public List<Integer> getAssos_id() {
        return null;
    }

    public void setAssos_id(List<Integer> assos_id) {

    }

    public List<Integer> getChats_id() {
        return null;
    }
    public void setChats_id(List<Integer> chats_id) {
    }

    public List<Integer> getEvents_id() {
        return null;
    }
    public void setEvents_id(List<Integer> events_id) {

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
     * This class is used to create a builder
     */
    public static final class UserBuilder{
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
        public UserBuilder() {};

        /**
         * Set the sciper // TODO cf number ?
         * @param sciper User sciper number
         */
        public void setSciper(String sciper) {
            this.sciper = sciper;
        }

        /**
         * User gaspar - username
         * @param gaspar gaspar
         */
        public void setGaspar(String gaspar) {
            this.gaspar = gaspar;
        }

        /**
         * User email
         * @param email email
         */
        public void setEmail(String email) {
            if(email.contains("@")){
                this.email = email;
            }
        }

        /**
         * User last names
         * @param last_names last names
         */
        public void setLast_names(String last_names) {
            this.last_names = last_names;
        }

        /**
         * User last names
         * @param first_names last names
         */
        public void setFirst_names(String first_names) {
            this.first_names = first_names;
        }

        /**
         * This function create a User and return the builded child
         * @return User Return a child of User
         */
        public User build() {
            User user = buildAuthenticatedUser();
            if(user != null) {
                return user;
            }

            return new Guest();
        }

        /**
         * Build an AuthenticatedUser
         * @return AuthenticatedUser or null
         */
        public AuthenticatedUser buildAuthenticatedUser() {
            if( this.sciper != null
                    &&  this.email  != null
                    &&  this.gaspar != null
                    &&  this.first_names != null
                    &&  this.last_names != null ){
                return new AuthenticatedUser(this.sciper, this.gaspar, this.email, this.first_names, this.last_names);
            }

            return null;
        }

        /**
         * Build guest user
         * @return Guest
         */
        public Guest buildGuestUser() {
            return new Guest();
        }
    }
}
