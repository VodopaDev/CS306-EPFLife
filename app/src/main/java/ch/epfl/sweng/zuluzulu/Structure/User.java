package ch.epfl.sweng.zuluzulu.Structure;

import java.util.List;

public class User {

    /**
     * This is the user ID, it is guaranteed to be unique.
     */
    private final String sciper;

    private final String gaspar;

    private final String email;

    private int id;
    private String first_names;
    private String last_names;

    private List<Integer> assos_id;
    private List<Integer> chats_id;
    private List<Integer> events_id;

    // TODO: Get data from cloud service using the id
    public User(int id) {
        this.sciper = null;
        this.id = id;
        this.gaspar = null;
        email = null;
    }

    // TODO: Add a method to add/remove one Association to assos_id, same for chats and events
    // TODO: Check inputs before changing fields
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_names() {
        return first_names;
    }
    public void setFirst_names(String first_names) {
        this.first_names = first_names;
    }

    public String getLast_names() {
        return last_names;
    }
    public void setLast_names(String last_names) {
        this.last_names = last_names;
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
}
