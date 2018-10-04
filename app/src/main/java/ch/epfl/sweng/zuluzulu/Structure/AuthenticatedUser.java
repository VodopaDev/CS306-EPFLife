package ch.epfl.sweng.zuluzulu.Structure;

import java.util.ArrayList;
import java.util.List;

public final class AuthenticatedUser extends User {
    /**
     * This is the user ID, it is guaranteed to be unique.
     */
    private final String sciper;

    /**
     * Gaspar account - it's the username
     */
    private final String gaspar;

    /**
     * User email
     */
    private final String email;

    /**
     * User first names (he can have few first names)
     */
    private final String first_names;

    /**
     * User last names, same remark as first_names
     */
    private final String last_names;

    // TODO ??? A commenter
    private List<Integer> assos_id;
    private List<Integer> chats_id;
    private List<Integer> events_id;

    // TODO: Get data from cloud service using the id
    protected AuthenticatedUser(String sciper, String gaspar, String email, String first_names, String last_names) {
        this.sciper = sciper;
        this.gaspar = gaspar;
        this.email = email;
        this.first_names = first_names;
        this.last_names = last_names;

        assos_id = new ArrayList<>();
        chats_id = new ArrayList<>();
        events_id = new ArrayList<>();
    }

    // TODO: Add a method to add/remove one Association to assos_id, same for chats and events
    // TODO: Check inputs before changing fields

    public String getFirst_names() {
        return first_names;
    }

    public String getLast_names() {
        return last_names;
    }

    public List<Integer> getAssos_id() {
        return assos_id;
    }

    public void setAssos_id(List<Integer> assos_id) {
        this.assos_id = assos_id;
    }

    public List<Integer> getChats_id() {
        return chats_id;
    }

    public void setChats_id(List<Integer> chats_id) {
        this.chats_id = chats_id;
    }

    public List<Integer> getEvents_id() {
        return events_id;
    }

    public void setEvents_id(List<Integer> events_id) {
        this.events_id = events_id;
    }

    public String getEmail() {
        return email;
    }

    public String getGaspar() {
        return gaspar;
    }

    public String getSciper() {
        return sciper;
    }
}
