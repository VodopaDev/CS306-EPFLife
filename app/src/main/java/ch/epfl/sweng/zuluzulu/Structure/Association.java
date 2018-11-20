package ch.epfl.sweng.zuluzulu.Structure;

import android.net.Uri;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.R;

/**
 * A simple class describing an Association
 * Has diverse getters and some functions to create views
 */
public class Association implements Serializable {
    public final static List<String> FIELDS = Arrays.asList("id", "name", "short_desc", "long_desc");

    private int id;
    private String name;
    private String short_desc;
    private String long_desc;

    private String icon_uri;
    private String banner_uri;

    private List<Map<String, Object>> events;
    private int channel_id;
    private int closest_event_id;

    public Association(int id, String name, String short_desc, String long_desc, Uri icon_uri, Uri banner_uri, List<Map<String, Object>> events, int channel_id, int closest_event_id){
        this.id = id;
        this.name = name;
        this.short_desc = short_desc;
        this.long_desc = long_desc;
        this.icon_uri = icon_uri;
        this.banner_uri = banner_uri;
        this.events = events;
        this.channel_id = channel_id;
        this.closest_event_id = closest_event_id;
    }

    /**
     * Create an association using a Firebase adapted map
     *
     * @param data the adapted map containing the association data
     * @throws IllegalArgumentException if the map isn't an Association's map
     */
    public Association(FirebaseMapDecorator data) {
        if (!data.hasFields(FIELDS)) {
            throw new IllegalArgumentException();
        }

        id = data.getInteger("id");
        name = data.getString("name");
        short_desc = data.getString("short_desc");
        long_desc = data.getString("long_desc");

        // Init the main chat id
        channel_id = data.get("channel_id") == null ?
                0 :
                data.getInteger("channel_id");

        // Init the upcoming event
        events = data.get("events") == null ?
                Collections.EMPTY_LIST :
                (List<Map<String, Object>>) data.get("events");
        computeClosestEvent();

        // Init the Icon URI
        String icon_str = data.getString("icon_uri");
        Uri uri = icon_str == null ?
                Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_icon) :
                Uri.parse(icon_str);
        icon_uri = uri == null ? null : uri.toString();


        // Init the Banner URI
        String banner_str = data.getString("banner_uri");
        uri = banner_str == null ?
                Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_banner) :
                Uri.parse(banner_str);
        banner_uri = uri == null ? null : uri.toString();
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
     * Return the Association's main chat id
     *
     * @return
     */
    public int getChannelId() {
        return channel_id;
    }

    /**
     * Return the Association's icon Uri
     *
     * @return the icon Uri
     */
    public String getIconUri() {
        return icon_uri;
    }

    /**
     * Return the Association's closest event happening id
     *
     * @return
     */
    public int getClosestEventId() {
        return closest_event_id;
    }

    /**
     * Compute the closest event id and store it in the class
     */
    private void computeClosestEvent() {
        if (events.isEmpty())
            closest_event_id = 0;
        else {
            int closest = ((Long) events.get(0).get("id")).intValue();
            java.util.Date closest_time = (java.util.Date) events.get(0).get("start");
            for (int i = 1; i < events.size(); i++) {
                java.util.Date current = (java.util.Date) events.get(i).get("start");
                if (current.before(closest_time)) {
                    closest = ((Long) events.get(i).get("id")).intValue();
                }
            }
            closest_event_id = closest;
        }
    }

    /**
     * Return the Association's banner Uri
     *
     * @return the banner Uri
     */
    public String getBannerUri() {
        return banner_uri;
    }
}
