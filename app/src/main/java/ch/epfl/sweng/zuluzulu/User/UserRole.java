package ch.epfl.sweng.zuluzulu.User;

/**
 * This enum represent the differents roles for the user
 */
public enum UserRole {
    USER, MODERATOR, ADMIN;

    public static UserRole getRoleFromString(String role){
        switch(role.toLowerCase().trim()){
            case "user":
                return USER;
            case "moderator":
                return MODERATOR;
            case "admin":
                return ADMIN;
            default:
                return USER;
        }
    }
}
