package ch.epfl.sweng.zuluzulu.Structure;

import android.location.Location;
import android.media.Image;

import java.util.List;

public class Association {

    private int id;
    private String name;
    private String description;
    private Image icon;

    private Location pos;
    private List<Integer> admins;

    private int main_chat_id;
    private List<Integer> chats;
    private List<Integer> events;

    // TODO: Get data from cloud service using the id;
    public Association(int id){
        this.id = id;
    }


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

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Image getIcon() {
        return icon;
    }
    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public Location getPos() {
        return pos;
    }
    public void setPos(Location pos) {
        this.pos = pos;
    }

    public List<Integer> getAdmins() {
        return admins;
    }
    public void setAdmins(List<Integer> admins) {
        this.admins = admins;
    }

    public int getMain_chat_id() {
        return main_chat_id;
    }
    public void setMain_chat_id(int main_chat_id) {
        this.main_chat_id = main_chat_id;
    }

    public List<Integer> getChats() {
        return chats;
    }
    public void setChats(List<Integer> chats) {
        this.chats = chats;
    }

    public List<Integer> getEvents() {
        return events;
    }
    public void setEvents(List<Integer> events) {
        this.events = events;
    }
}
