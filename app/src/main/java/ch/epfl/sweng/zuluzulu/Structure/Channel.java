package ch.epfl.sweng.zuluzulu.Structure;

import android.net.Uri;

import com.google.firebase.firestore.GeoPoint;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Utility.Utils;

/**
 * Class that represents a channel in a view
 */
public class Channel extends FirebaseStructure {
    private static final double MAX_DISTANCE_TO_ACCESS_CHANNEL = 50;
    private static final double MAX_DISTANCE_TO_SEE_CHANNEL = 1000;
    private String name;
    private String shortDescription;
    private Map<String, Object> restrictions;
    private String iconUri;

    private boolean isAccessible = true;
    private double distance = 0;

    public Channel(String id, String name, String shortDescription, Map<String, Object> restrictions, String iconUri) {
        super(id);
        this.name = name;
        this.shortDescription = shortDescription;
        this.restrictions = new HashMap<>(restrictions);
        this.iconUri = iconUri;
    }

    public Channel(FirebaseMapDecorator data) {
        super(data);
        if (!data.hasFields(requiredFields()))
            throw new IllegalArgumentException();

        this.name = data.getString("name");
        this.shortDescription = data.getString("short_description");
        this.restrictions = data.getMap("restrictions");

        // Init the Icon URI
        this.iconUri = data.getString("icon_uri");
    }

    public static List<String> requiredFields() {
        return Arrays.asList("id", "name", "short_description", "restrictions");
    }

    @Override
    public Map<String, Object> getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getId());
        map.put("name", name);
        map.put("restrictions", restrictions);
        map.put("icon_uri", iconUri);
        map.put("short_description", shortDescription);
        return map;
    }

    /**
     * Getter for the name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the shortDescription
     *
     * @return the shortDescription
     */
    public String getShortDescription() {
        return shortDescription;
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
     * Return if the channel is accessible
     *
     * @return if the channel is accessible
     */
    public boolean isAccessible() {
        return isAccessible;
    }

    /**
     * Return the distance
     *
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Check whether a user can access to this channel or not
     *
     * @param userSection The user section
     * @return whether the user can access it or not
     */
    public boolean canBeSeenBy(String userSection, GeoPoint userLocation) {
        String section = restrictions.containsKey("section") ? (String) restrictions.get("section") : null;
        GeoPoint channelLocation = restrictions.containsKey("location") ? (GeoPoint) restrictions.get("location") : null;
        return hasGoodSection(section, userSection) && hasGoodLocation(channelLocation, userLocation);
    }

    /**
     * Check if a user has the correct section to see a channel
     *
     * @param requestSection expected section
     * @param userSection    user's section
     * @return true if requestSection and userSection are the same or if there is no requestSection, false otherwise
     */
    private boolean hasGoodSection(String requestSection, String userSection) {
        if (requestSection == null)
            return true;

        return requestSection.equals(userSection);
    }

    /**
     * Check if a user has the correct location to see a channel
     *
     * @param requestedLocation expected location
     * @param userLocation      user's location
     * @return true if requestedLocation and userLocation are the same or if there is no requestedLocation, false otherwise
     */
    private boolean hasGoodLocation(GeoPoint requestedLocation, GeoPoint userLocation) {
        if (requestedLocation == null)
            return true;

        if (userLocation == null)
            return false;

        distance = Utils.distanceBetween(requestedLocation, userLocation);
        double diff_distance = distance - MAX_DISTANCE_TO_ACCESS_CHANNEL;
        isAccessible = distance < MAX_DISTANCE_TO_ACCESS_CHANNEL;

        return (diff_distance <= MAX_DISTANCE_TO_SEE_CHANNEL);
    }
}
