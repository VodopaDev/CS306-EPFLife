package ch.epfl.sweng.zuluzulu.Structure;

import java.util.List;

public class User {
    private int id;
    private String first_name;
    private String last_name;

    private List<Integer> assos_id;
    private List<Integer> chats_id;
    private List<Integer> events_id;

    // TODO: Get data from cloud service using the id
    public User(int id) {
        this.id = id;
    }

    // TODO: Add a method to add/remove one Association to assos_id, same for chats and events
    // TODO: Check inputs before changing fields
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
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
