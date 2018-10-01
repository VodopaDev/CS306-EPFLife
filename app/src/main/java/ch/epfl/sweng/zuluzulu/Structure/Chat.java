package ch.epfl.sweng.zuluzulu.Structure;

import java.util.List;

public class Chat {

    private int id;
    private String name;

    private int event_id;
    private int asso_id;
    private List<String> messages;
    private List<Integer> users;
    private List<Integer> admins;

    // TODO: Get data from cloud service using the id
    public Chat(int id) {
        this.id = id;
    }

    // TODO: Add a method to add/remove one User from users or admins, same for messages
    // TODO: Check inputs before changing fields
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getEvent_id() {
        return event_id;
    }
    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public int getAsso_id() {
        return asso_id;
    }
    public void setAsso_id(int asso_id) {
        this.asso_id = asso_id;
    }

    public List<String> getMessages() {
        return messages;
    }
    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public List<Integer> getUsers() {
        return users;
    }
    public void setUsers(List<Integer> users) {
        this.users = users;
    }

    public List<Integer> getAdmins() {
        return admins;
    }
    public void setAdmins(List<Integer> admins) {
        this.admins = admins;
    }
}
