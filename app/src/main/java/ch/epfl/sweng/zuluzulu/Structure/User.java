package ch.epfl.sweng.zuluzulu.Structure;

import java.util.List;

abstract public class User {


    public final class UserBuilder{
        /**
         * This is the user ID, it is guaranteed to be unique.
         */
        private String sciper;

        /**
         * Gaspar account - it's the username // TODO was in string in Profile. See if it's logic
         */
        private String gaspar;

        /**
         * User email
         */
        private String email;

        /**
         * User ID
         */
        private int id;

        /**
         * User first names (he can have few first names)
         */
        private String first_names;

        /**
         * User last names, same remark as first_names
         */
        private String last_names;

        public UserBuilder() {
            setId(0);
        }

        public void setSciper(String sciper) {
            this.sciper = sciper;
        }

        public void setGaspar(String gaspar) {
            this.gaspar = gaspar;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setLast_names(String last_names) {
            this.last_names = last_names;
        }

        public void setFirst_names(String first_names) {
            this.first_names = first_names;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
