package ch.epfl.sweng.zuluzulu.Structure;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.HashMap;
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

    private String iconUri;
    private String bannerUri;

    private List<Map<String, Object>> upcomingEvents;
    private String channelId;
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

        // Init the main chat id
        channelId = data.getString("channel_id");

        // Init the upcoming event
        upcomingEvents = (List<Map<String, Object>>) data.get("upcoming_events");
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
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * Return the Association's main chat id
     *
     * @return
     */
    @Nullable
    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId){
        this.channelId = channelId;
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
        if (upcomingEvents == null || upcomingEvents.isEmpty())
            closestEventId = null;
        else {
            String closest = (String) upcomingEvents.get(0).get("id");
            java.util.Date closest_time = (java.util.Date) upcomingEvents.get(0).get("start");
            for (int i = 1; i < upcomingEvents.size(); i++) {
                java.util.Date current = (java.util.Date) upcomingEvents.get(i).get("start");
                if (current.before(closest_time)) {
                    closest = (String) upcomingEvents.get(i).get("id");
                }
            }
            closestEventId = closest;
        }
    }

    public Map<String,Object> getData(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", getId());
        map.put("name", name);
        map.put("short_description", shortDescription);
        map.put("icon_uri", iconUri);
        map.put("banner_uri", bannerUri);
        map.put("upcoming_events", upcomingEvents);
        map.put("channel_id", channelId);
        return map;
    }

    public static List<String> requiredFields(){
        return Arrays.asList("id", "name", "short_description");
    }

    @Override
    public int compareTo(@NonNull Association o) {
        return name.compareTo(o.getName());
    }

}
