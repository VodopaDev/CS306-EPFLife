package ch.epfl.sweng.zuluzulu.Structure;

import android.net.Uri;

import com.google.firebase.firestore.GeoPoint;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Utility.Utils;

/**
 * Class that represents a channel in a view
 */
public class Channel extends FirebaseStructure {
    private static final double MAX_DISTANCE_TO_ACCESS_CHANNEL = 50;
    private static final double MAX_DISTANCE_TO_SEE_CHANNEL = 500;
    private String name;
    private String shortDescription;
    private Map<String, Object> restrictions;
    private String iconUri;

    private boolean isAccessible;
    private double distance;

    public Channel(FirebaseMapDecorator data) {
        super(data);
        if (!data.hasFields(requiredFields()))
            throw new IllegalArgumentException();

        this.name = data.getString("name");
        this.shortDescription = data.getString("short_description");
        this.restrictions = data.getMap("restrictions");
        this.isAccessible = true;
        this.distance = 0;

        // Init the Icon URI
        this.iconUri = data.getString("icon_uri");
    }

    @Override
    public Map<String, Object> getData() {
        Map<String, Object> map = new HashMap();
        map.put("id", getId());
        map.put("name", name);
        map.put("restrictions", restrictions);
        map.put("icon_uri", getIconUri().toString());
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
     * Setter for the name
     */
    public void setName(String name) {
        this.name = name;
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
     * Setter for the shortDescription
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
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

    public boolean isAccessible() {
        return isAccessible;
    }

    public double getDistance() {
        return distance;
    }

    /**
     * Check whether a user can access to this channel or not
     *
     * @param user The user who wants to enter the channel
     * @return whether the user can access it or not
     */
    public boolean canBeSeenBy(AuthenticatedUser user, GeoPoint userLocation) {
        String section = (String) restrictions.get("section");
        GeoPoint channelLocation = (GeoPoint) restrictions.get("location");
        return hasGoodSection(section, user.getSection()) && hasGoodLocation(channelLocation, userLocation);
    }

    private boolean hasGoodSection(String requestSection, String userSection) {
        if (requestSection == null)
            return true;

        return requestSection.equals(userSection);
    }

    private boolean hasGoodLocation(GeoPoint requestedLocation, GeoPoint userLocation) {
        if (requestedLocation == null || userLocation == null)
            return true;

        distance = Utils.distanceBetween(requestedLocation, userLocation);
        double diff_distance = distance - MAX_DISTANCE_TO_ACCESS_CHANNEL;
        isAccessible = distance < MAX_DISTANCE_TO_ACCESS_CHANNEL;

        return (diff_distance <= MAX_DISTANCE_TO_SEE_CHANNEL);
    }

    public static List<String> requiredFields(){
        return Arrays.asList("id", "name", "short_description", "restrictions");
    }
}
