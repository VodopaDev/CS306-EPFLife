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
    private String shortDescription;
    private String longDescription;

    private String iconUri;
    private String bannerUri;

    private List<Map<String, Object>> events;
    private String mainChannelId;
    private String closestEventId;

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
        shortDescription = data.getString("short_description");
        longDescription = data.getString("long_description");

        // Init the main chat id
        mainChannelId = data.getString("channel_id");

        // Init the upcoming event
        events = (List<Map<String, Object>>) data.get("events");
        computeClosestEvent();

        // Init the Icon URI
        iconUri = data.getString("icon_uri");

        // Init the Banner URI
        bannerUri = data.getString("banner_uri");
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
        return shortDescription;
    }

    /**
     * Return the Association's long description
     *
     * @return the long description
     */
    public String getLongDesc() {
        return longDescription;
    }

    /**
     * Return the Association's main chat id
     *
     * @return
     */
    @Nullable
    public String getChannelId() {
        return mainChannelId;
    }

    /**
     * Return the Association's icon Uri
     *
     * @return the icon Uri
     */
    public Uri getIconUri() {
        return iconUri == null ?
                Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_icon) :
                Uri.parse(iconUri);
    }

    /**
     * Return the Association's banner Uri
     *
     * @return the banner Uri
     */
    public Uri getBannerUri() {
        return bannerUri == null ?
                Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_banner) :
                Uri.parse(bannerUri);
    }

    /**
     * Return the Association's closest event happening id
     *
     * @return
     */
    @Nullable
    public String getClosestEventId() {
        return closestEventId;
    }

    /**
     * Compute the closest event id and store it in the class
     */
    private void computeClosestEvent() {
        if (events == null || events.isEmpty())
            closestEventId = null;
        else {
            String closest = (String)events.get(0).get("id");
            java.util.Date closest_time = (java.util.Date) events.get(0).get("start");
            for (int i = 1; i < events.size(); i++) {
                java.util.Date current = (java.util.Date) events.get(i).get("start");
                if (current.before(closest_time)) {
                    closest = (String) events.get(i).get("id");
                }
            }
            closestEventId = closest;
        }
    }


    public static List<String> requiredFields(){
        return Arrays.asList("id", "name", "short_description", "long_description");
    }

    @Override
    public int compareTo(@NonNull Association o) {
        return name.compareTo(o.getName());
    }
}
