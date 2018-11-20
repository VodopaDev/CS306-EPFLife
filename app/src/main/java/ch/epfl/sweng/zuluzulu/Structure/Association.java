package ch.epfl.sweng.zuluzulu.Structure;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.R;

/**
 * A simple class describing an Association
 * Has diverse getters and some functions to create views
 */
public class Association extends FirebaseStructure implements Comparable<Association> {

    private String name;
    private String short_desc;
    private String long_desc;

    private Uri icon_uri;
    private Uri banner_uri;

    private List<Map<String, Object>> events;
    private Long channel_id;
    private Long closest_event_id;

    /**
     * Create an association using a Firebase adapted map
     *
     * @param data the adapted map containing the association data
     * @throws IllegalArgumentException if the map isn't an Association's map
     */
    public Association(FirebaseMapDecorator data) {
        super(data);
        if (!data.hasFields(requiredFields())) {
            throw new IllegalArgumentException();
        }

        name = data.getString("name").trim();
        short_desc = data.getString("short_desc");
        long_desc = data.getString("long_desc");

        // Init the main chat id
        channel_id = data.get("channel_id") == null ?
                0L :
                data.getLong("channel_id");

        // Init the upcoming event
        events = data.get("events") == null ?
                Collections.EMPTY_LIST :
                (List<Map<String, Object>>) data.get("events");
        computeClosestEvent();

        // Init the Icon URI
        String icon_str = data.getString("icon_uri");
        icon_uri = icon_str == null ?
                Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_icon) :
                Uri.parse(icon_str);

        // Init the Banner URI
        String banner_str = data.getString("banner_uri");
        banner_uri = banner_str == null ?
                Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_banner) :
                Uri.parse(banner_str);
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
    public Long getChannelId() {
        return channel_id;
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

    /**
     * Return the Association's closest event happening id
     *
     * @return
     */
    public Long getClosestEventId() {
        return closest_event_id;
    }

    /**
     * Compute the closest event id and store it in the class
     */
    private void computeClosestEvent() {
        if (events.isEmpty())
            closest_event_id = 0L;
        else {
            Long closest = (Long)events.get(0).get("id");
            java.util.Date closest_time = (java.util.Date) events.get(0).get("start");
            for (int i = 1; i < events.size(); i++) {
                java.util.Date current = (java.util.Date) events.get(i).get("start");
                if (current.before(closest_time)) {
                    closest = (Long) events.get(i).get("id");
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
    @Nullable
    public Uri getBannerUri() {
        return banner_uri;
    }

    public static List<String> requiredFields(){
        return Arrays.asList("id", "name", "short_desc", "long_desc");
    }

    @Override
    public int compareTo(@NonNull Association o) {
        return name.compareTo(o.getName());
    }
}
