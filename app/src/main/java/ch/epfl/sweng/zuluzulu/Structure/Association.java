package ch.epfl.sweng.zuluzulu.Structure;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
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
    private String longDescription;

    private String iconUri;
    private String bannerUri;

    private List<String> upcomingEvents;
    private String channelId;

    public Association(String id, String name, String short_desc, String long_desc, String icon_uri, String banner_uri, List<String> events, String channel_id) {
        super(id);
        this.name = name;
        this.shortDescription = short_desc;
        this.longDescription = long_desc;
        this.iconUri = icon_uri;
        this.bannerUri = banner_uri;
        this.upcomingEvents = events;
        this.channelId = channel_id;
    }

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
        channelId = data.getString("channel_id");

        // Init the upcoming event
        upcomingEvents = data.getStringList("upcoming_events");

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

    public String getLongDescription(){
        return longDescription;
    }

    public List<String> getUpcomingEvents(){
        if(upcomingEvents == null)
            return new ArrayList<>();
        return upcomingEvents;
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

    @Override
    public Map<String,Object> getData(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", getId());
        map.put("name", name);
        map.put("short_description", shortDescription);
        map.put("icon_uri", iconUri);
        map.put("banner_uri", bannerUri);
        map.put("upcoming_events", upcomingEvents);
        map.put("channel_id", channelId);
		map.put("long_description", longDescription);
        return map;
    }

    @NonNull
    public static List<String> requiredFields(){
        return Arrays.asList("id", "name", "short_description");
    }

    @Override
    public int compareTo(@NonNull Association o) {
        return name.compareTo(o.getName());
    }

}
