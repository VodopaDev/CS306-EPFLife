package ch.epfl.sweng.zuluzulu.Structure;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.R;

/**
 * A simple class describing an Association
 * Has diverse getters and some functions to create views
 */
public class Association implements Serializable {
    private List<String> firebase_fields = Arrays.asList("id","name","short_desc","long_desc");

    private int id;
    private String name;
    private String short_desc;
    private String long_desc;

    private Uri icon_uri;
    private Uri banner_uri;

    private List<Map<String, Object>> events;
    private int closest_event_id;

    /**
     * Create an association using a DocumentSnapshot
     *
     * @param snap the document snapshot
     * @throws IllegalArgumentException if the snapshot isn't an Association's snapshot
     */
    public Association(DocumentSnapshot snap) {
        if (!Utils.isValidSnapshot(snap, firebase_fields))
            throw new IllegalArgumentException();

        id = snap.getLong("id").intValue();
        name = snap.getString("name");
        short_desc = snap.getString("short_desc");
        long_desc = snap.getString("long_desc");
        events = snap.get("events") == null ?
                new ArrayList<Map<String, Object>>() :
                (List<Map<String, Object>>)snap.get("events");

        closest_event_id = computeClosestEvent();

        // Init the Icon URI
        String icon_str = snap.getString("icon_uri");
        icon_uri = icon_str == null ?
                Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_icon):
                Uri.parse(icon_str);

        // Init the Banner URI
        String banner_str = snap.getString("banner_uri");
        banner_uri = banner_str == null ?
                Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_banner):
                Uri.parse(banner_str);
    }

    /**
     * Return a Comparator for two Associations using their names
     *
     * @return compareTo of two Associations names
     */
    public static Comparator<Association> getComparator() {
        return new Comparator<Association>() {
            @Override
            public int compare(Association o1, Association o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };
    }

    /**
     * Return the Association's id
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Return the Association's name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Return the Association's short description
     *
     * @return the short description
     */
    public String getShortDesc() {
        return short_desc;
    }

    /**
     * Return the Association's long description
     *
     * @return the long description
     */
    public String getLongDesc() {
        return long_desc;
    }

    /**
     * Return the Association's icon Uri
     *
     * @return the icon Uri
     */
    @Nullable
    public Uri getIconUri() {
        return icon_uri;
    }

    public int getClosestEventId(){
        return closest_event_id;
    }

    private int computeClosestEvent(){
        if(events.isEmpty())
            return 0;
        else{
            int closest = ((Long)events.get(0).get("id")).intValue();
            Timestamp closest_time = (Timestamp)events.get(0).get("start");
            for(int i = 1; i < events.size(); i++){
                Timestamp current = (Timestamp)events.get(i).get("start");
                if(current.toDate().before(closest_time.toDate())){
                    closest = ((Long)events.get(i).get("id")).intValue();
                }
            }
            return closest;
        }
    }

    /**
     * Return the Association's banner Uri
     *
     * @return the banner Uri
     */
    @Nullable
    public Uri getBannerUri() {
        return banner_uri;
    }
}
