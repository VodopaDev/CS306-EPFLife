package ch.epfl.sweng.zuluzulu.Structure;

import android.location.Location;
import android.media.Image;
import android.net.Uri;

import java.util.Date;
import java.util.List;

public class Event {

    private int id;
    private String name;
    private String short_desc;
    private String long_desc;

    private static final String IMAGE_PATH = "events/ev";
    private static final String ICON_EXT = "_icon.png";
    private Uri icon;

    private int chat_id;
    private int asso_id;
    private List<Integer> admins;

    private Location pos;
    private Date start_date;
    private Date end_date;

    // TODO: Get data from cloud service using the id
    public Event(int id) {
        this.id = id;
    }

    // TODO: Add a method to add/remove one User to admins (instead of getting/setting the admins list)
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

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String description) {
        this.short_desc = description;
    }

    public String getLong_desc() {
        return long_desc;
    }

    public void setLong_desc(String description) {
        this.long_desc = description;
    }

    public Uri getIcon() {
        return icon;
    }

    public void setIcon(Uri icon) {
        this.icon = icon;
    }

    public int getChat_id() {
        return chat_id;
    }

    public void setChat_id(int chat_id) {
        this.chat_id = chat_id;
    }

    public int getAsso_id() {
        return asso_id;
    }

    public void setAsso_id(int asso_id) {
        this.asso_id = asso_id;
    }

    public List<Integer> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Integer> admins) {
        this.admins = admins;
    }

    public Location getPos() {
        return pos;
    }

    public void setPos(Location pos) {
        this.pos = pos;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

}
