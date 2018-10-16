package ch.epfl.sweng.zuluzulu.Structure;

import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class Event implements Serializable {

    private int id;
    private String name;
    private String short_desc;
    private String long_desc;

    private static final String IMAGE_PATH = "events/event";
    private static final String ICON_EXT = "_icon.png";
    private Uri icon_uri;

    private int chat_id;
    private int asso_id;
    private List<Integer> admins;

    private Location pos;
    private Date start_date;
    private Date end_date;

    /**
     * Create an event using a DocumentSnapshot
     * @param snap the document snapshot
     * @throws IllegalArgumentException if the snapshot isn't an Event's snapshot
     */
    public Event(DocumentSnapshot snap) {
        if(!snapshotIsValid(snap))
            throw new NullPointerException();

        id = ((Long) snap.get("id")).intValue();
        name = snap.getString("name");
        short_desc = snap.getString("short_desc");
        long_desc = snap.getString("long_desc");
        icon_uri = Uri.parse(snap.getString("icon_uri"));
        start_date = snap.getDate("start_date");
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

    public Uri getIconUri() {
        return icon_uri;
    }

    public void setIconUri(Uri icon) {
        this.icon_uri = icon;
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

    /**
     * Check if a DocumentSnapshot correspond to an Event's one
     * @param snap the DocumentSnapshot
     * @return true if it is a valid snapshot, false otherwise
     */
    private boolean snapshotIsValid(DocumentSnapshot snap){
        return !(snap == null
                || snap.get("id") == null
                || snap.getString("short_desc") == null
                || snap.getString("long_desc") == null
                || snap.getString("name") == null
                || snap.getString("icon_uri") == null
                || snap.getDate("start_date") == null
        );
    }

    public static Comparator<Event> getComparator(){
        return new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };
    }

}
