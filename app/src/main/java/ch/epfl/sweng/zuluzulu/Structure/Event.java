package ch.epfl.sweng.zuluzulu.Structure;

import android.location.Location;
import android.net.Uri;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

import ch.epfl.sweng.zuluzulu.R;

public class Event implements Serializable {

    private static final String IMAGE_PATH = "events/event";
    private static final String ICON_EXT = "_icon.png";
    private String id;
    private String assos_name;
    private String short_desc;
    private String long_desc;
    private Uri banner_uri;
    private Uri icon_uri;

    private int chat_id;
    private int asso_id;
    private List<Integer> admins;

    private Location pos;

    private Timestamp start_date_timestamp;
    private String start_date;


    /**
     * Create an event using a DocumentSnapshot
     *
     * @param snap the document snapshot
     * @throws IllegalArgumentException if the snapshot isn't an Event's snapshot
     */
    public Event(DocumentSnapshot snap) {
        if (!snapshotIsValid(snap))
            throw new NullPointerException();

        id = snap.getId();
        assos_name = snap.getString("name");
        short_desc = snap.getString("short_desc");
        long_desc = snap.getString("long_desc");

        String icon_str = snap.getString("icon_uri");
        icon_uri = icon_str == null ?
                Uri.parse("android.ressource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_icon) :
                Uri.parse(icon_str);

        start_date_timestamp = snap.getTimestamp("start_date");
        start_date = Utils.dateFormat.format(snap.getTimestamp("start_date").toDate());
    }

    // maybe use id instead of name
    public static Comparator<Event> assoNameComparator() {
        return new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };
    }

    public static Comparator<Event> dateComparator() {
        return new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o1.getStartDateTimestamp().compareTo(o2.getStartDateTimestamp());
            }
        };
    }

    // TODO: Add a method to add/remove one User to admins (instead of getting/setting the admins list)
    // TODO: Check inputs before changing fields
    public String getId() {
        return id;
    }

//    public void setId(String id) {
//        this.id = id;
//    }

    public String getName() {
        return assos_name;
    }

//    public void setName(String assos_name) {
//        this.assos_name = assos_name;
//    }

    public String getShort_desc() {
        return short_desc;
    }

//    public void setShort_desc(String description) {
//        this.short_desc = description;
//    }

    public Uri getIcon() {
        return banner_uri;
    }

//    public void setIcon(Uri banner_uri) {
//        this.banner_uri = banner_uri;
//    }
//
//    public int getChat_id() {
//        return chat_id;
//    }
//
//    public void setChat_id(int chat_id) {
//        this.chat_id = chat_id;
//    }
//
//    public int getAsso_id() {
//        return asso_id;
//    }
//
//    public void setAsso_id(int asso_id) {
//        this.asso_id = asso_id;
//    }
//
//    public List<Integer> getAdmins() {
//        return admins;
//    }
//
//    public void setAdmins(List<Integer> admins) {
//        this.admins = admins;
//    }
//
//    public Location getPos() {
//        return pos;
//    }
//
//    public void setPos(Location pos) {
//        this.pos = pos;
//    }

    public String getLong_desc() {
        return long_desc;
    }

//    public void setLong_desc(String description) {
//        this.long_desc = description;
//    }
//
//    public void setLong_desc(String description) {
//        this.long_desc = description;
//    }

    public Uri getIconUri() {
        return icon_uri;
    }

    public String getStart_date() {
        return start_date;
    }

    public Timestamp getStartDateTimestamp() {
        return start_date_timestamp;
    }
//
//    public void setStart_date(Date start_date) {
//        this.start_date = start_date;
//    }
//
//    public Date getEnd_date() {
//        return end_date;
//    }
//
//    public void setEnd_date(Date end_date) {
//        this.end_date = end_date;
//    }
//
//    public void setIconUri(Uri icon) {
//        this.icon_uri = icon;
//    }
//
//    public int getChat_id() {
//        return chat_id;
//    }
//
//    public void setChat_id(int chat_id) {
//        this.chat_id = chat_id;
//    }
//
//    public int getAsso_id() {
//        return asso_id;
//    }
//
//    public void setAsso_id(int asso_id) {
//        this.asso_id = asso_id;
//    }
//
//    public List<Integer> getAdmins() {
//        return admins;
//    }
//
//    public void setAdmins(List<Integer> admins) {
//        this.admins = admins;
//    }
//
//    public Location getPos() {
//        return pos;
//    }
//
//    public void setPos(Location pos) {
//        this.pos = pos;
//    }
//


    /**
     * Check if a DocumentSnapshot correspond to an Event's one
     *
     * @param snap the DocumentSnapshot
     * @return true if it is a valid snapshot, false otherwise
     */
    private boolean snapshotIsValid(DocumentSnapshot snap) {
        return !(snap == null
                || snap.get("id") == null
                || snap.getString("short_desc") == null
                || snap.getString("long_desc") == null
                || snap.getString("name") == null
                || snap.getString("icon_uri") == null
                || snap.getDate("start_date") == null
        );
    }

}
