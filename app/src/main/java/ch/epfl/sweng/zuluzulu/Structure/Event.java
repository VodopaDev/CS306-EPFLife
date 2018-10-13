package ch.epfl.sweng.zuluzulu.Structure;

import android.location.Location;
import android.media.Image;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Date;
import java.util.List;

public class Event {

    private int id;
    private String name;
    private String short_desc;
    private String long_desc;

    private static final String IMAGE_PATH = "events/event";
    private static final String ICON_EXT = "_icon.png";
    private Uri icon;

    private int chat_id;
    private int asso_id;
    private List<Integer> admins;

    private Location pos;
    private Date start_date;
    private Date end_date;

    // TODO: Get data from cloud service using the id
    public Event(DocumentSnapshot snap) {
        this(snap, null);
    }

    /**
     * Create an event using a DocumentSnapshot
     * @param snap the document snapshot
     * @throws IllegalArgumentException if the snapshot isn't an Event's snapshot
     */
    public Event(DocumentSnapshot snap, Uri iconUri) {
        if(!snapshotIsValid(snap))
            throw new NullPointerException();

        id = ((Long) snap.get("id")).intValue();
        name = snap.getString("name");
        short_desc = snap.getString("short_desc");
        long_desc = snap.getString("long_desc");

        if(iconUri == null) {
            FirebaseStorage.getInstance()
                    .getReference()
                    .child(IMAGE_PATH + id + ICON_EXT)
                    .getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            icon = uri;
                        }
                    });
        }
        else{
            icon = iconUri;
        }
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
